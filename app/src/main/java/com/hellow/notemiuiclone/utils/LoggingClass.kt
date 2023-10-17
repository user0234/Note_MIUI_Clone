package com.hellow.notemiuiclone.utils

import android.annotation.SuppressLint
import android.util.Log

object LoggingClass {

    @SuppressLint("LogNotTimber")
    fun logI(data:String){
        Log.i("Normal log","data")
    }

    fun logTagI(tag:String,data:String) {
        Log.i(tag, data)
    }

}