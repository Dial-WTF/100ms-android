package live.hms.roomkit.util

import android.content.Context
import android.media.MediaPlayer
import androidx.annotation.RawRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

class MediaPlayerManager(
    private val lifecycle: Lifecycle
) : LifecycleEventObserver {

    private var mediaPlayer: MediaPlayer? = null

    init {
        lifecycle.addObserver(this)
    }

    private val onMediaPlayerPrepared = MediaPlayer.OnPreparedListener {
        try {
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)
                && lifecycle.currentState != Lifecycle.State.DESTROYED
            )
                mediaPlayer?.start()
        } catch (e: Exception) {
        }
    }


    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_DESTROY, Lifecycle.Event.ON_STOP -> {
                releaseMediaPlayer()
            }
            else -> { /* No action needed for other events */ }
        }
    }

    private fun setSource(@RawRes raw: Int, context: Context) {
        try {
            mediaPlayer = MediaPlayer.create(context,raw).apply {
                start()
            }
        } catch (e: Exception) {
            val a = e
        }
    }

    private fun releaseMediaPlayer() {
        try {
            if (mediaPlayer != null) {
                if (mediaPlayer?.isPlaying == true) {
                    mediaPlayer?.stop()
                }
                mediaPlayer?.release()
                mediaPlayer = null
            }
        } catch (e: Exception) {
        }
    }

    /**
     * Public API
     */
    fun startPlay(@RawRes raw: Int, context: Context) {
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)
            && lifecycle.currentState != Lifecycle.State.DESTROYED
        ) {
            try {
                releaseMediaPlayer()
                setSource(raw, context)
            } catch (e: Exception) {
            }
        }
    }

    fun pause() {
        if (mediaPlayer?.isPlaying == true)
            mediaPlayer?.pause()
    }

    fun resume() {
        if (mediaPlayer?.isPlaying?.not() == true)
            mediaPlayer?.start();
    }

    fun stop() {
        mediaPlayer?.stop()
    }
}
