package tech.taskroulette.app.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tech.taskroulette.app.domain.model.GameSession
import tech.taskroulette.app.domain.model.Settings
import tech.taskroulette.app.domain.model.Task
import tech.taskroulette.app.domain.usecase.BuildWheelSectorsUseCase
import tech.taskroulette.app.domain.usecase.CreateGameSessionUseCase
import tech.taskroulette.app.domain.usecase.session.GetGameSessionUseCase
import tech.taskroulette.app.domain.usecase.session.InsertGameSessionUseCase
import tech.taskroulette.app.domain.usecase.session.ObserveGameSessionsUseCase
import tech.taskroulette.app.domain.usecase.settings.ObserveSettingsUseCase
import tech.taskroulette.app.domain.usecase.spin.PlanSpinUseCase
import tech.taskroulette.app.domain.usecase.spin.SpinPlan
import tech.taskroulette.app.domain.usecase.task.ObserveTasksUseCase
import tech.taskroulette.app.domain.wheel.WheelSector

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    observeTasksUseCase: ObserveTasksUseCase,
    observeSettingsUseCase: ObserveSettingsUseCase,
    observeGameSessionsUseCase: ObserveGameSessionsUseCase,
    private val buildWheelSectorsUseCase: BuildWheelSectorsUseCase,
    private val planSpinUseCase: PlanSpinUseCase,
    private val createGameSessionUseCase: CreateGameSessionUseCase,
    private val insertGameSessionUseCase: InsertGameSessionUseCase,
    private val getGameSessionUseCase: GetGameSessionUseCase,
) : ViewModel() {

    private val _state: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    private val _events: MutableSharedFlow<HomeEvent> = MutableSharedFlow(extraBufferCapacity = 1)
    val events: SharedFlow<HomeEvent> = _events.asSharedFlow()

    init {
        observeSettingsUseCase()
            .onEach { settings ->
                _state.update { current -> current.copy(settings = settings) }
            }
            .launchIn(viewModelScope)

        observeSettingsUseCase()
            .map { it.activeTaskSetId }
            .distinctUntilChanged()
            .flatMapLatest { taskSetId -> observeTasksUseCase(taskSetId) }
            .onEach { tasks ->
                _state.update { current ->
                    if (current.replaySessionId != null) {
                        current.copy(tasks = tasks)
                    } else {
                        current.copy(
                            tasks = tasks,
                            wheelTasks = tasks,
                            sectors = buildWheelSectorsUseCase.build(tasks),
                        )
                    }
                }
            }
            .launchIn(viewModelScope)

        observeGameSessionsUseCase()
            .onEach { sessions ->
                _state.update { current ->
                    current.copy(
                        activeTaskTitle = sessions.firstOrNull()?.selectedTitleOrNull(),
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onReplayRequested(sessionId: String?): Unit {
        if (sessionId.isNullOrBlank()) {
            _state.update { current ->
                current.copy(
                    replaySessionId = null,
                    wheelTasks = current.tasks,
                    sectors = buildWheelSectorsUseCase.build(current.tasks),
                )
            }
            return
        }

        viewModelScope.launch {
            val session = getGameSessionUseCase(sessionId) ?: return@launch
            val activeTaskSetId = _state.value.settings.activeTaskSetId
            val replayTasks = session.tasksSnapshot
                .sortedBy { it.orderIndex }
                .map { snap ->
                    Task(
                        id = snap.originalTaskId ?: snap.id,
                        title = snap.title,
                        colorArgb = snap.colorArgb,
                        weight = snap.weight,
                        taskSetId = activeTaskSetId,
                    )
                }

            _state.update { current ->
                current.copy(
                    replaySessionId = sessionId,
                    wheelTasks = replayTasks,
                    sectors = buildWheelSectorsUseCase.build(replayTasks),
                )
            }
        }
    }

    fun onSpinClick(currentRotationDegrees: Float): Unit {
        val snapshot = _state.value
        if (snapshot.isSpinning) return
        val plan = planSpinUseCase.plan(
            tasks = snapshot.wheelTasks,
            currentRotationDegrees = currentRotationDegrees,
        ) ?: return

        _state.update { current ->
            current.copy(
                isSpinning = true,
                spinPlan = plan,
                highlightTaskId = null,
            )
        }
    }

    fun onSpinAnimationFinished(): Unit {
        val snapshot = _state.value
        val plan = snapshot.spinPlan ?: return

        viewModelScope.launch {
            val createdAt = System.currentTimeMillis()
            val session = createGameSessionUseCase.createFromTasks(
                tasks = snapshot.wheelTasks,
                selectedTaskId = plan.selectedTask.id,
                createdAtEpochMillis = createdAt,
            )
            insertGameSessionUseCase(session)

            _state.update { current ->
                current.copy(
                    isSpinning = false,
                    spinPlan = null,
                    highlightTaskId = plan.selectedTask.id,
                    // After replay spin, return to normal task list.
                    replaySessionId = null,
                    wheelTasks = current.tasks,
                    sectors = buildWheelSectorsUseCase.build(current.tasks),
                )
            }

            _events.tryEmit(HomeEvent.NavigateToResult(sessionId = session.id))
        }
    }

    private fun GameSession.selectedTitleOrNull(): String? =
        tasksSnapshot.firstOrNull { it.id == selectedSnapshotTaskId }?.title
}

data class HomeUiState(
    val tasks: List<Task> = emptyList(),
    val wheelTasks: List<Task> = emptyList(),
    val sectors: List<WheelSector> = emptyList(),
    val settings: Settings = Settings.Default,
    val isSpinning: Boolean = false,
    val spinPlan: SpinPlan? = null,
    val highlightTaskId: String? = null,
    val replaySessionId: String? = null,
    val activeTaskTitle: String? = null,
)

sealed class HomeEvent {
    data class NavigateToResult(val sessionId: String) : HomeEvent()
}


