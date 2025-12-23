package tech.taskroulette.app.domain.model

data class Settings(
    val isSoundEnabled: Boolean,
    val isHapticsEnabled: Boolean,
) {
    companion object {
        val Default: Settings = Settings(
            isSoundEnabled = true,
            isHapticsEnabled = true,
        )
    }
}


