package com.devwarex.currency.models

data class ConversionQueryModel(
    val amount: Double,
    val from: String,
    val to: String
)
