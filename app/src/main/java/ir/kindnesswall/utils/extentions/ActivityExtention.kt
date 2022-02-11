package ir.kindnesswall.utils.extentions

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.core.content.getSystemService


/**
 * Created by Farshid Abazari since 25/10/19
 *
 * Usage:
 *
 * How to call:
 *
 * Useful parameter:
 *
 */

fun Activity.hideKeyboard() {
    val inputMethodManager = getSystemService<InputMethodManager>() ?: return

    if (inputMethodManager.isAcceptingText)
        inputMethodManager.hideSoftInputFromWindow(
            currentFocus?.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
}