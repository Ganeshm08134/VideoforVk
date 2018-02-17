package akhmedoff.usman.videoforvk.player

import android.media.AudioManager
import com.google.android.exoplayer2.ExoPlayer

class AudioFocusListener(private val player: ExoPlayer?) : AudioManager.OnAudioFocusChangeListener {
    override fun onAudioFocusChange(focusChange: Int) {
        when (focusChange) {
            AudioManager.AUDIOFOCUS_LOSS -> player?.playWhenReady = false

        //AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> player?.set
        }
    }
}