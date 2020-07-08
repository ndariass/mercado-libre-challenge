package com.example.mercadolibre_ui.extension

import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView

/**
 * [TextView] extension to set the given text. If the text is null the view is hidden
 *
 * @param content the text to set
 */
fun TextView.setTextOrHide(content: String?) {
    content?.apply {
        visibility = VISIBLE
        text = content
    } ?: run { visibility = GONE }
}
