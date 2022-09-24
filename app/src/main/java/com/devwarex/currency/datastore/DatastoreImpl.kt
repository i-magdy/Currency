package com.devwarex.currency.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = "currency_app_datastore")

class DatastoreImpl @Inject constructor(private val context: Context) {

    companion object{

        private const val REFRESH_SYMBOLS_TIMEOUT: String = "user_refresh_time"

        fun create(context: Context): DatastoreImpl{
            return DatastoreImpl(context)
        }

        private val REFRESH_SYMBOLS_TIMEOUT_KEY = longPreferencesKey(REFRESH_SYMBOLS_TIMEOUT)
    }


    val symbolTimeout: Flow<Long> get() = context.datastore.data.map { it[REFRESH_SYMBOLS_TIMEOUT_KEY] ?: 0L }
    suspend fun updateSymbolsTimeout(time: Long) = context.datastore.edit { it[REFRESH_SYMBOLS_TIMEOUT_KEY] = time }

}