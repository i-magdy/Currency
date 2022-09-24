package com.devwarex.currency.api

import com.devwarex.currency.util.EndPoints
import retrofit2.http.GET

interface CurrencyService {


    @GET(EndPoints.ALL_CURRENCIES_RES)
    suspend fun getAllCurrenciesSymbol()

    @GET(EndPoints.CONVERT_CURRENCY_RES)
    suspend fun currencyConversion()

    @GET(EndPoints.TIME_SERIES_RES)
    suspend fun getHistoricalCurrencyConversionRate()

    @GET(EndPoints.LATEST_RATES_RES)
    suspend fun getLatestCurrenciesRates()
}