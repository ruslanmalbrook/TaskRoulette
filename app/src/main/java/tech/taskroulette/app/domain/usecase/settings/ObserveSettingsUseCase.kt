package tech.taskroulette.app.domain.usecase.settings

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import tech.taskroulette.app.domain.model.Settings
import tech.taskroulette.app.domain.repository.SettingsRepository

class ObserveSettingsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
) {
    operator fun invoke(): Flow<Settings> = settingsRepository.settings
}


