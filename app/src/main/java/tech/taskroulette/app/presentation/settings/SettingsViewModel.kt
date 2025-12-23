package tech.taskroulette.app.presentation.settings

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
import tech.taskroulette.app.domain.model.ConfettiStyle
import tech.taskroulette.app.domain.model.Settings
import tech.taskroulette.app.domain.model.SpinSoundTheme
import tech.taskroulette.app.domain.repository.SettingsRepository
import tech.taskroulette.app.domain.usecase.session.ObserveGameSessionsUseCase
import tech.taskroulette.app.domain.usecase.settings.ObserveSettingsUseCase
import tech.taskroulette.app.domain.usecase.stats.ComputeStatsUseCase
import tech.taskroulette.app.domain.usecase.stats.Stats

@HiltViewModel
class SettingsViewModel @Inject constructor(
    observeSettingsUseCase: ObserveSettingsUseCase,
    observeGameSessionsUseCase: ObserveGameSessionsUseCase,
    private val settingsRepository: SettingsRepository,
    private val computeStatsUseCase: ComputeStatsUseCase,
) : ViewModel() {

    private val _state: MutableStateFlow<SettingsUiState> = MutableStateFlow(SettingsUiState())
    val state: StateFlow<SettingsUiState> = _state.asStateFlow()

    init {
        observeSettingsUseCase()
            .onEach { settings ->
                _state.update { current -> current.copy(settings = settings) }
            }
            .launchIn(viewModelScope)

        observeGameSessionsUseCase()
            .onEach { sessions ->
                _state.update { current ->
                    current.copy(stats = computeStatsUseCase.compute(sessions))
                }
            }
            .launchIn(viewModelScope)
    }

    fun onSoundEnabledChange(isEnabled: Boolean): Unit {
        viewModelScope.launch {
            settingsRepository.setSoundEnabled(isEnabled)
        }
    }

    fun onHapticsEnabledChange(isEnabled: Boolean): Unit {
        viewModelScope.launch {
            settingsRepository.setHapticsEnabled(isEnabled)
        }
    }

    fun onSoundThemeChange(theme: SpinSoundTheme): Unit {
        viewModelScope.launch {
            settingsRepository.setSpinSoundTheme(theme)
        }
    }

    fun onConfettiStyleChange(style: ConfettiStyle): Unit {
        viewModelScope.launch {
            settingsRepository.setConfettiStyle(style)
        }
    }
}

data class SettingsUiState(
    val settings: Settings = Settings.Default,
    val stats: Stats = Stats(
        totalSpins = 0,
        mostFrequentTaskTitle = null,
    ),
)


