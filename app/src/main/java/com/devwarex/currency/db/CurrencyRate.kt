package com.devwarex.currency.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rates_table")
data class CurrencyRate(
    @PrimaryKey val rateKey: String,
    val base: String,
    val to: String,
    val rate: Double,
    val timestamp: Long
)
