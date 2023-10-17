package com.hellow.notemiuiclone.audioRecorder

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import java.io.File
import java.io.FileOutputStream
import java.util.Timer
import java.util.TimerTask

class AndroidAudioRecorder(private val context: Context
): AudioRecorder {

    private var recorder: MediaRecorder? = null
    private var amplitudeTimer = Timer()
    private val AMPLITUDE_UPDATE_MS = 75L
    private var durationUpdate:Long = 0

    private fun createRecorder(): MediaRecorder {
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else MediaRecorder()
    }


    override fun start(outputFile: File) {
        createRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(FileOutputStream(outputFile).fd)
            prepare()
            start()
            recorder = this
        }
        startAmplitudeUpdates()
    }

   private fun startAmplitudeUpdates() {
        amplitudeTimer.cancel()
        amplitudeTimer = Timer()
        amplitudeTimer.scheduleAtFixedRate(getAmplitudeUpdateTask(), 0, AMPLITUDE_UPDATE_MS)
    }

    private fun getAmplitudeUpdateTask() = object : TimerTask() {
        override fun run() {
            if (recorder != null) {
                try {
                    durationUpdate += 75
                    onPlayAmplitude?.let { it(recorder!!.maxAmplitude,durationUpdate) }
                } catch (ignored: Exception) {
                }
            }
        }
    }


    override fun stop():Long {
        recorder?.stop()
        recorder?.reset()
        recorder = null
        amplitudeTimer.cancel()
        amplitudeTimer.purge()
        val duration = durationUpdate
        durationUpdate = 0
        return duration
    }


    private var onPlayAmplitude: ((Int?,Long) -> Unit)? = null

    fun setonPlayAmplitude(listener: (Int?,Long) -> Unit) {
        onPlayAmplitude = listener
    }

}