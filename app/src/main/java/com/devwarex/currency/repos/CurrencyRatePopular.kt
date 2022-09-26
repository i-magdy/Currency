package com.devwarex.currency.repos

import com.devwarex.currency.api.ApiResource
import com.devwarex.currency.api.CurrencyService
import com.devwarex.currency.data.PopularCurrencies
import com.devwarex.currency.di.NamedApiKey
import com.devwarex.currency.models.LatestRatesModel
import kotlinx.coroutines.channels.Channel
import javax.inject.Inject

class CurrencyRatePopular @Inject constructor(
    private val service: CurrencyService,
    @NamedApiKey private val apiKey: String
) {
    val popularRates = Channel<ApiResource<LatestRatesModel>>()

    suspend fun getPopularRates(base: String){
        try {
            popularRates.send(ApiResource.create(
                response = service.getLatestCurrenciesRates(
                    base = base,
                    symbols = PopularCurrencies.getSymbols(),
                    apiKey = apiKey
                )
            ))
        }catch (e: Exception){
            popularRates.send(ApiResource.create(e))
        }
    }
}