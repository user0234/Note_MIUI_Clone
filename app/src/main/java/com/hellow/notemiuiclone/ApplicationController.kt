package com.hellow.notemiuiclone

import android.app.Application
import android.util.Log



class ApplicationController : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {

        }
        Log.i("ApplicationClass","Application Started not in debug")
    }
}