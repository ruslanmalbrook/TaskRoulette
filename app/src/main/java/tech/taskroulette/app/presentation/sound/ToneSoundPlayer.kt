package tech.taskroulette.app.presentation.sound

import android.media.AudioManager
import android.media.ToneGenerator
import tech.taskroulette.app.domain.model.SpinSoundTheme

class ToneSoundPlayer {
    private val toneGenerator: ToneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 70)

    fun playTick(theme: SpinSoundTheme): Unit {
        val (tone, durationMs) = when (theme) {
            SpinSoundTheme.Classic -> ToneGenerator.TONE_PROP_BEEP to 20
            SpinSoundTheme.Soft -> ToneGenerator.TONE_PROP_PROMPT to 20
            SpinSoundTheme.Arcade -> ToneGenerator.TONE_PROP_BEEP2 to 20
        }
        toneGenerator.startTone(tone, durationMs)
    }

    fun playStop(theme: SpinSoundTheme): Unit {
        val (tone, durationMs) = when (theme) {
            SpinSoundTheme.Classic -> ToneGenerator.TONE_PROP_ACK to 120
            SpinSoundTheme.Soft -> ToneGenerator.TONE_PROP_ACK to 90
            SpinSoundTheme.Arcade -> ToneGenerator.TONE_PROP_NACK to 140
        }
        toneGenerator.startTone(tone, durationMs)
    }

    fun release(): Unit {
        toneGenerator.release()
    }
}


