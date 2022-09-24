package com.devwarex.currency.models

class CurrencyConversionModel(
    val date: String,
    val info: ConversionInfoModel,
    val query: ConversionQueryModel,
    val result: Double,
    val success: String
)