package com.devwarex.currency.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency_table")
data class Currency(
    @PrimaryKey val symbol: String,
    val name: String
)