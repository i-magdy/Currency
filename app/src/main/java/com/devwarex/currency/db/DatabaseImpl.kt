package com.devwarex.currency.db

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DatabaseImpl @Inject constructor(
    private var db: AppDao
) {

    val currencies: Flow<List<String>> get() = db.getAllCurrencies()
    fun getCurrenciesExcept(symbol: String): Flow<List<String>> = db.getCurrenciesExcept(symbol = symbol)

    suspend fun insertSymbols(symbols: Map<String,String>){
        for (s in symbols.entries){
            db.insertSymbols(
                currency = Currency(
                    symbol = s.key,
                    name = s.value
                )
            )
        }
    }

}