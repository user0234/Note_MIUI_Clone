package com.hellow.notemiuiclone.utils.sharedPref

import android.content.Context

object SharedPrefFunctions {

    fun getSharedPrefInt(
        context: Context,
        tag: String,
        key: String,
        defaultValue: Int,
    ): Int {
        val sharedPref = context.getSharedPreferences(tag, Context.MODE_PRIVATE)
        return sharedPref.getInt(key, defaultValue)
    }

    fun putSharedPrefInt(context: Context, tag: String, key: String, value: Int) {
        val sharedPref = context.getSharedPreferences(tag, Context.MODE_PRIVATE)
        sharedPref.edit().putInt(key, value).apply()
    }


}