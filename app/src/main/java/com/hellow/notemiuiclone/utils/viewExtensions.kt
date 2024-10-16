package com.hellow.notemiuiclone.utils

import android.view.View
import android.content.Context
import android.util.TypedValue
import android.view.inputmethod.InputMethodManager
/**
 * Try to hide the keyboard from [this] view.
 */
fun View.hideKeyboard() {
    val context = this.context ?: return
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}

/**
 * Try to show the keyboard from [this] view.
 * The keyboard is shown with a 200 ms delay by default, otherwise it often doesn't work.
 */
fun View.showKeyboard(delay: Long = 200L) {
    val context = this.context ?: return
    this.postDelayed({
        val focus = this.findFocus() ?: return@postDelayed
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(focus, 0)
    }, delay)
}

/**
 *
 * convert the given dp to pixels
 *
 */

fun Context.toDip(dp: Int): Float = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    dp.toFloat(),
    resources.displayMetrics)


/**
 *
 * convert the given sp to pixels
 *
 */

fun Context.toSip(sp: Int): Float = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_SP,
    sp.toFloat(),
    resources.displayMetrics)