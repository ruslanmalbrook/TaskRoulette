package tech.taskroulette.app.presentation.result

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tech.taskroulette.app.domain.model.Settings
import tech.taskroulette.app.domain.usecase.session.GetGameSessionUseCase
import tech.taskroulette.app.domain.usecase.settings.ObserveSettingsUseCase
import tech.taskroulette.app.presentation.navigation.TaskRouletteRoute

@HiltViewModel
class ResultViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getGameSessionUseCase: GetGameSessionUseCase,
    observeSettingsUseCase: ObserveSettingsUseCase,
) : ViewModel() {

    private val sessionId: String? = savedStateHandle[TaskRouletteRoute.Result.ARG_SESSION_ID]

    private val _state: MutableStateFlow<ResultUiState> = MutableStateFlow(
        ResultUiState(
            sessionId = sessionId,
        ),
    )
    val state: StateFlow<ResultUiState> = _state.asStateFlow()

    init {
        observeSettingsUseCase()
            .onEach { settings ->
                _state.update { current -> current.copy(settings = settings) }
            }
            .launchIn(viewModelScope)

        viewModelScope.launch {
            val id = sessionId ?: return@launch
            val session = getGameSessionUseCase(id) ?: return@launch
            val selected = session.tasksSnapshot.firstOrNull { it.id == session.selectedSnapshotTaskId }

            _state.update { current ->
                current.copy(
                    selectedTaskTitle = selected?.title,
                    selectedTaskColorArgb = selected?.colorArgb,
                )
            }
        }
    }
}

data class ResultUiState(
    val sessionId: String?,
    val settings: Settings = Settings.Default,
    val selectedTaskTitle: String? = null,
    val selectedTaskColorArgb: Int? = null,
)


