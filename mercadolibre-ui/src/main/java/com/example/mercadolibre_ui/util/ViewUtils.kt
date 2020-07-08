package com.example.mercadolibre_ui.util

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * Hides the keyboard
 *
 * @param view any view that is currently visible to get the window token from
 */
fun hideKeyboard(view: View) {
    val imm = view.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.hideSoftInputFromWindow(view.windowToken, 0)
}
