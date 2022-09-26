package com.devwarex.currency.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface AppDao {

    @Query("select symbol from currency_table")
    fun getAllCurrencies():Flow<List<String>>

    @Query("select symbol from currency_table where symbol != :symbol")
    fun getCurrenciesExcept(symbol: String):Flow<List<String>>

    @Query("select * from rates_table where rateKey = :rateKey and timestamp > :time")
    fun getCurrencyRate(rateKey: String,time: Long): Flow<CurrencyRate?>

    @Query("select * from rates_table where rateKey = :rateKey")
    suspend fun getCurrencyRateByKey(rateKey: String): CurrencyRate?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSymbols(currency: Currency)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRate(rate: CurrencyRate)


}