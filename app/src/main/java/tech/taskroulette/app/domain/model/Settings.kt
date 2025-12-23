package tech.taskroulette.app.domain.model

data class Settings(
    val isSoundEnabled: Boolean,
    val isHapticsEnabled: Boolean,
    val spinSoundTheme: SpinSoundTheme,
    val confettiStyle: ConfettiStyle,
) {
    companion object {
        val Default: Settings = Settings(
            isSoundEnabled = true,
            isHapticsEnabled = true,
            spinSoundTheme = SpinSoundTheme.Classic,
            confettiStyle = ConfettiStyle.Classic,
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


