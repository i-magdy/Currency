package com.devwarex.currency.models

data class LatestRatesModel(
    val base: String,
    val date: String,
    val rates: Map<String,Double>,
    val success: Boolean,
    val timestamp: Long
)
