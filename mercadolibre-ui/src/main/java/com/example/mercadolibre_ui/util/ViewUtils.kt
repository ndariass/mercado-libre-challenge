package com.example.mercadolibre_ui.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

/**
 * Hides the keyboard
 *
 * @param view any view that is currently visible to get the window token from
 */
fun hideKeyboard(view: View) {
    val imm = view.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.hideSoftInputFromWindow(view.windowToken, 0)
}

/**
 * Shows the keyboard
 *
 * @param editText the edit text view that requests to display the keyboard
 */
fun showKeyboard(editText: EditText) {
    val imm = editText.context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
}
