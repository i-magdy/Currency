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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSymbols(currency: Currency)

}