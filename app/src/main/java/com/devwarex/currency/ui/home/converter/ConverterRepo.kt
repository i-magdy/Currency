package com.devwarex.currency.ui.home.converter

import com.devwarex.currency.db.DatabaseImpl
import javax.inject.Inject

class ConverterRepo @Inject constructor(
    private val db: DatabaseImpl
) {

    val currencies get() = db.currencies
    fun getCurrenciesExcept(symbol: String) = db.getCurrenciesExcept(symbol = symbol)

}