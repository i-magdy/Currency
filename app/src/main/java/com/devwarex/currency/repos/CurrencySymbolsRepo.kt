package com.devwarex.currency.repos

import android.util.Log
import com.devwarex.currency.api.ApiResource
import com.devwarex.currency.api.CurrencyService
import com.devwarex.currency.di.NamedApiKey
import com.devwarex.currency.models.CurrenciesSymbolModel
import kotlinx.coroutines.channels.Channel
import javax.inject.Inject

class CurrencySymbolsRepo @Inject constructor(
    private val service: CurrencyService,
    @NamedApiKey private val apiKey: String
) {

    val symbols = Channel<ApiResource<CurrenciesSymbolModel>>()


    suspend fun getAllCurrencies(){
        try {
            symbols.send(ApiResource.create(service.getAllCurrenciesSymbol(apiKey = apiKey)))
        }catch (e: Exception){
            symbols.send(ApiResource.create(e))
        }
    }
}