package com.devwarex.currency.repos

import com.devwarex.currency.api.ApiResource
import com.devwarex.currency.api.CurrencyService
import com.devwarex.currency.di.NamedApiKey
import com.devwarex.currency.models.TimeSeriesConversionModel
import kotlinx.coroutines.channels.Channel
import javax.inject.Inject

class CurrencyRateTimeSeries @Inject constructor(
    private val service: CurrencyService,
    @NamedApiKey private val apiKey: String
) {

    val ratesTimeSeries = Channel<ApiResource<TimeSeriesConversionModel>>()

    suspend fun getCurrencyRatesTimeSeries(
        base: String,
        to: String,
        start: String,
        end: String
    ){
        try {
            ratesTimeSeries.send(ApiResource.create(
                response = service.getHistoricalCurrencyConversionRate(
                    apiKey = apiKey,
                    base = base,
                    to = to,
                    startDate = start,
                    endDate = end
                )
            ))
        }catch (e: Exception){
            ratesTimeSeries.send(ApiResource.create(e))
        }
    }
}