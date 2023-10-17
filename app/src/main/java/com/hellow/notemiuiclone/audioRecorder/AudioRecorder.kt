package com.hellow.notemiuiclone.audioRecorder

import java.io.File

interface AudioRecorder {
    fun start(outputFile: File)
    fun stop():Long
}