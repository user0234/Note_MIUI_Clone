package com.hellow.notemiuiclone.utils.widget

import android.content.res.Resources

class AndroidUtils {

    companion object {

        fun dpToPx(dp: Int): Float {
            return dpToPx(dp.toFloat())
        }
        fun dpToPx(dp: Float): Float {
            return dp * Resources.getSystem().displayMetrics.density
        }

    }



}