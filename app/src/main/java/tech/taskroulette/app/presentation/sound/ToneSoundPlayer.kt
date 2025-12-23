package tech.taskroulette.app.presentation.sound

import android.media.AudioManager
import android.media.ToneGenerator

class ToneSoundPlayer {
    private val toneGenerator: ToneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 70)

    fun playTick(): Unit {
        toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP, 20)
    }

    fun playStop(): Unit {
        toneGenerator.startTone(ToneGenerator.TONE_PROP_ACK, 120)
    }

    fun release(): Unit {
        toneGenerator.release()
    }
}


