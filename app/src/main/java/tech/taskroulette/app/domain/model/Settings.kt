package tech.taskroulette.app.domain.model

data class Settings(
    val isSoundEnabled: Boolean,
    val isHapticsEnabled: Boolean,
    val spinSoundTheme: SpinSoundTheme,
    val confettiStyle: ConfettiStyle,
    val activeTaskSetId: String,
) {
    companion object {
        val Default: Settings = Settings(
            isSoundEnabled = true,
            isHapticsEnabled = true,
            spinSoundTheme = SpinSoundTheme.Classic,
            confettiStyle = ConfettiStyle.Classic,
            activeTaskSetId = TaskSet.DEFAULT_ID,
        )
    }
}

enum class SpinSoundTheme {
    Classic,
    Soft,
    Arcade,
}

enum class ConfettiStyle {
    Classic,
    Pop,
    Streamers,
}


