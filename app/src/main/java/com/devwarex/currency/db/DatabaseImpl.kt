package com.devwarex.currency.db

import com.devwarex.currency.models.CurrencyConversionModel
import com.devwarex.currency.util.TimeoutUtil
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DatabaseImpl @Inject constructor(
    private var db: AppDao
) {

    val currencies: Flow<List<String>> get() = db.getAllCurrencies()
    fun getCurrenciesExcept(symbol: String): Flow<List<String>> = db.getCurrenciesExcept(symbol = symbol)
    fun getCurrencyRate(rateKey: String,time: Long): Flow<CurrencyRate?> = db.getCurrencyRate(rateKey = rateKey, time = time)
    suspend fun getCurrencyRateByKey(rateKey: String) = db.getCurrencyRateByKey(rateKey)
    suspend fun insertRate(rateModel: CurrencyConversionModel?){
        if (rateModel == null) return
        val timestamp = TimeoutUtil.convertServerTimeToLocal(rateModel.info.timestamp)
        db.insertRate(
            CurrencyRate(
                rateKey = "${rateModel.query.from}-${rateModel.query.to}",
                base = rateModel.query.from,
                to = rateModel.query.to,
                timestamp = timestamp,
                rate = rateModel.info.rate
            )
        )
        db.insertRate(
            CurrencyRate(
                rateKey = "${rateModel.query.to}-${rateModel.query.from}",
                base = rateModel.query.to,
                to = rateModel.query.from,
                timestamp = timestamp,
                rate = 1/rateModel.info.rate
            )
        )

    }
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