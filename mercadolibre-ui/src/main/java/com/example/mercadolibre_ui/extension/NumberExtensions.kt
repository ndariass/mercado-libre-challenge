package com.example.mercadolibre_ui.extension

import java.text.DecimalFormat

private val decimalFormat = DecimalFormat("#,###")

fun Float.formatNoDecimals(): String = decimalFormat.format(this)