package com.devwarex.currency.repos

import com.devwarex.currency.api.ApiResource
import com.devwarex.currency.api.CurrencyService
import com.devwarex.currency.di.NamedApiKey
import com.devwarex.currency.models.CurrencyConversionModel
import kotlinx.coroutines.channels.Channel
import javax.inject.Inject

class CurrencyConversionRepo @Inject constructor(
    private val service: CurrencyService,
    @NamedApiKey private val apiKey: String
) {

    val rate = Channel<ApiResource<CurrencyConversionModel>>()

    suspend fun convertCurrency(from: String,to: String){
        try {
            rate.send(ApiResource.create(service.currencyConversion(
                apiKey = apiKey,
                from = from,
                to = to
            )))
        }catch (e: Exception){
            rate.send(ApiResource.create(e))
        }
    }
}