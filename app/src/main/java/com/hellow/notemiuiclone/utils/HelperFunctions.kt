package com.hellow.notemiuiclone.utils

import java.util.UUID

object HelperFunctions {

    fun getFileName() = UUID.randomUUID().toString()


    fun getTimeInUnit(time:Long):String {
        if(time<1000){
            return "1 sec"
        }

        if(time<60000){
            return "${time/1000} sec"
        }

        return if(time<3600000){
            "${time/60000} min"

        } else "${time/36000000} hour"
    }
}