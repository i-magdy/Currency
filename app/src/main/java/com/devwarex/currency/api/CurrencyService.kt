package com.devwarex.currency.api

import com.devwarex.currency.models.CurrenciesSymbolModel
import com.devwarex.currency.models.CurrencyConversionModel
import com.devwarex.currency.models.LatestRatesModel
import com.devwarex.currency.models.TimeSeriesConversionModel
import com.devwarex.currency.util.EndPoints
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface CurrencyService {


    @GET(EndPoints.ALL_CURRENCIES_RES)
    suspend fun getAllCurrenciesSymbol(
        @Header(EndPoints.API_KEY_HEADER) apiKey: String
    ): Response<CurrenciesSymbolModel>

    @GET(EndPoints.CONVERT_CURRENCY_RES)
    suspend fun currencyConversion(
        @Header(EndPoints.API_KEY_HEADER) apiKey: String,
        @Query(EndPoints.FROM_QUERY) from: String,
        @Query(EndPoints.TO_QUERY) to: String,
        @Query(EndPoints.AMOUNT_QUERY) amount: Int = 1
    ): Response<CurrencyConversionModel>

    @GET(EndPoints.TIME_SERIES_RES)
    suspend fun getHistoricalCurrencyConversionRate(
        @Header(EndPoints.API_KEY_HEADER) apiKey: String,
        @Query(EndPoints.BASE_CURRENCY_QUERY) base: String,
        @Query(EndPoints.TO_QUERY) to: String,
        @Query(EndPoints.START_DATE_QUERY) startDate: String,
        @Query(EndPoints.END_DATE_QUERY) endDate: String
    ): Response<TimeSeriesConversionModel>

    @GET(EndPoints.LATEST_RATES_RES)
    suspend fun getLatestCurrenciesRates(
        @Header(EndPoints.API_KEY_HEADER) apiKey: String,
        @Query(EndPoints.BASE_CURRENCY_QUERY) base: String,
        @Query(EndPoints.SYMBOLS_QUERY) symbols: String
    ): Response<LatestRatesModel>
}