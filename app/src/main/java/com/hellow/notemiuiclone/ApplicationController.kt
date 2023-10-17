package com.hellow.notemiuiclone

import android.app.Application
import android.util.Log
import timber.log.Timber
import timber.log.Timber.Forest.plant


class ApplicationController : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            plant(Timber.DebugTree())
            Timber.i("application started")
        }
        Log.i("ApplicationClass","Application Started not in debug")
    }
}