package tech.taskroulette.app.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import tech.taskroulette.app.domain.model.GameSession
import tech.taskroulette.app.domain.usecase.session.ObserveGameSessionsUseCase

@HiltViewModel
class HistoryViewModel @Inject constructor(
    observeGameSessionsUseCase: ObserveGameSessionsUseCase,
) : ViewModel() {

    private val _state: MutableStateFlow<HistoryUiState> = MutableStateFlow(HistoryUiState())
    val state: StateFlow<HistoryUiState> = _state.asStateFlow()

    private val _events: MutableSharedFlow<HistoryEvent> = MutableSharedFlow(extraBufferCapacity = 1)
    val events: SharedFlow<HistoryEvent> = _events.asSharedFlow()

    init {
        observeGameSessionsUseCase()
            .onEach { sessions ->
                _state.update { current ->
                    current.copy(
                        sessions = sessions.map { it.toUi() },
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onSessionClick(sessionId: String): Unit {
        _events.tryEmit(HistoryEvent.NavigateToResult(sessionId))
    }

    fun onReplayClick(sessionId: String): Unit {
        _events.tryEmit(HistoryEvent.NavigateToReplay(sessionId))
    }

    private fun GameSession.toUi(): HistorySessionItem {
        val selected = tasksSnapshot.firstOrNull { it.id == selectedSnapshotTaskId }
        return HistorySessionItem(
            id = id,
            createdAtEpochMillis = createdAtEpochMillis,
            selectedTitle = selected?.title ?: "—",
        )
    }
}

data class HistoryUiState(
    val sessions: List<HistorySessionItem> = emptyList(),
)

data class HistorySessionItem(
    val id: String,
    val createdAtEpochMillis: Long,
    val selectedTitle: String,
)

sealed class HistoryEvent {
    data class NavigateToResult(val sessionId: String) : HistoryEvent()
    data class NavigateToReplay(val sessionId: String) : HistoryEvent()
}


