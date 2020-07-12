package com.example.mercadolibre_ui.extension

import java.text.DecimalFormat

private val decimalFormat = DecimalFormat("#,###")

/**
 * [Float] extension to format its value as a number without decimals
 *
 * @return a String with the formatted value
 */
fun Float.formatNoDecimals(): String = decimalFormat.format(this)
