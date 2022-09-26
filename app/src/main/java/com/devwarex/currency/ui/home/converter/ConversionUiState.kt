package com.devwarex.currency.ui.home.converter

data class ConversionUiState(
    val amount: String ="1",
    val result: String = "",
    val from: String = "",
    val to: String = "",
    val rate: Double = 10.0,
    val enable: Boolean = false
)
