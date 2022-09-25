package com.devwarex.currency.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy


@Dao
interface AppDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSymbols(currency: Currency)

}