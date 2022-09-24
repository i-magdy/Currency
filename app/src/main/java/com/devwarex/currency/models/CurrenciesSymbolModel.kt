package com.devwarex.currency.models

data class CurrenciesSymbolModel(
    val success: Boolean,
    val symbols: Map<String,String>
)
