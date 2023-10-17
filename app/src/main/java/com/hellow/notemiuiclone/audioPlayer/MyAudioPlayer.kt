package com.hellow.notemiuiclone.audioPlayer

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import com.chibde.visualizer.LineBarVisualizer
import java.util.Timer
import java.util.TimerTask

class MyAudioPlayer(private val context:Context): AudioPlayer {


    private var player:MediaPlayer? = null
    private var amplitudeTimer = Timer()
    private val AMPLITUDE_UPDATE_MS = 75L

    override fun playFile(fileUri: Uri, visualizer: LineBarVisualizer) {
         MediaPlayer.create(context,fileUri).apply {
             player = this
             start()
             visualizer.setPlayer(player!!.audioSessionId)
             onPlayerDuration?.let {
                 it(player!!.duration.toString())
             }
         }
        player!!.setOnCompletionListener {
            onPlayerStop?.let {
                it(1)
            }
            player!!.currentPosition
        }
        startDurationUpdates()
    }

    override fun stop() {
         player?.stop()
        player?.release()
        player = null
    }

    private var onPlayerStop: ((Int) -> Unit)? = null

    fun setonPlayerStop(listener: (Int) -> Unit) {
        onPlayerStop = listener
    }
    private var onPlayerDuration: ((String) -> Unit)? = null

    fun setonPlayerDuration(listener: (String) -> Unit) {
        onPlayerDuration = listener
    }

    fun startDurationUpdates() {
        amplitudeTimer.cancel()
        amplitudeTimer = Timer()
        amplitudeTimer.scheduleAtFixedRate(getAmplitudeUpdateTask(),  1000, 1000)
    }

    private fun getAmplitudeUpdateTask() = object : TimerTask() {
        override fun run() {
            if (player != null) {
                try {
                    onPlayAmplitude?.let { it(player!!.currentPosition) }
                } catch (ignored: Exception) {
                }
            }
        }
    }

    private var onPlayAmplitude: ((Int?) -> Unit)? = null

    fun setonPlayAmplitude(listener: (Int?) -> Unit) {
        onPlayAmplitude = listener
    }
}