package com.devwarex.currency.models

data class TimeSeriesConversionModel(
    val base: String,
    val end_date: String,
    val rates: Map<String,Map<String,Double>>,
    val start_date: String,
    val success: Boolean,
    val timeseries: Boolean
)
