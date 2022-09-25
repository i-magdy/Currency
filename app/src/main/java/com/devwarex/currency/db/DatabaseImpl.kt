package com.devwarex.currency.db

import javax.inject.Inject

class DatabaseImpl @Inject constructor(
    private var db: AppDao
) {

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