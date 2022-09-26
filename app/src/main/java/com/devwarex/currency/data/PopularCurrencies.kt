package com.devwarex.currency.data

object PopularCurrencies {

    private val list = listOf(
        "USD",
        "EUR",
        "JPY",
        "GBP",
        "AUD",
        "CAD",
        "CHF",
        "CNY",
        "HKD",
        "NZD"
    )

    fun getSymbols(): String = list.joinToString(",")
}