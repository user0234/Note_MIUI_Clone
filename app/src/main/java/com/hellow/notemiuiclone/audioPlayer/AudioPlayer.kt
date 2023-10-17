package com.hellow.notemiuiclone.audioPlayer

import android.net.Uri
import com.chibde.visualizer.LineBarVisualizer

interface AudioPlayer {

    fun playFile(fileUri: Uri, visualizer: LineBarVisualizer)

    fun stop()

}