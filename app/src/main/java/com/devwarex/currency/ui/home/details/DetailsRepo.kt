package com.devwarex.currency.ui.home.details

import com.devwarex.currency.api.ApiResource
import com.devwarex.currency.db.CurrencyRate
import com.devwarex.currency.db.DatabaseImpl
import com.devwarex.currency.models.RatesModel
import com.devwarex.currency.repos.CurrencyRatePopular
import com.devwarex.currency.repos.CurrencyRateTimeSeries
import com.devwarex.currency.util.ApiDateTimeModel
import com.devwarex.currency.util.ErrorState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailsRepo @Inject constructor(
    private val popularRepo: CurrencyRatePopular,
    private val timeSeriesRepo: CurrencyRateTimeSeries,
    private val db: DatabaseImpl
) {

    private val coroutineJob = CoroutineScope(Dispatchers.IO)
    private var toKey: String = ""
    val errorState = MutableStateFlow(ErrorState.NONE)
    val popularRates = MutableStateFlow<List<RatesModel>>(emptyList())
    val timeRates = MutableStateFlow<List<RatesModel>>(emptyList())
    private var errorCounts = 0

    init {
        coroutineJob.launch {
            launch {
                popularRepo.popularRates.receiveAsFlow().collect{
                    when(val api  = it){
                        is ApiResource.OnSuccess -> {
                            if (api.body != null){
                                if (api.body.success) {
                                    val base = api.body.base
                                    val list = ArrayList<RatesModel>()
                                    for (value in api.body.rates) {
                                        list.add(
                                            RatesModel(
                                                title = "$base/${value.key}",
                                                value = String.format("%.2f", value.value)
                                            )
                                        )
                                    }
                                    popularRates.emit(list)
                                }
                            }
                        }
                        is ApiResource.OnError -> {
                            if (errorCounts < 2){
                                errorState.emit(ErrorState.TRY_AGAIN)
                            }else{
                                errorState.emit(ErrorState.TRY_LATER)
                            }
                            errorCounts++
                        }
                        is ApiResource.OnLoading -> {}
                    }
                }
            }

            launch { timeSeriesRepo.ratesTimeSeries.receiveAsFlow().collect{
                when(val api  = it){
                    is ApiResource.OnSuccess -> {
                        if (api.body != null){
                            if (api.body.success) {
                                val base = api.body.base
                                val list = ArrayList<RatesModel>()
                                for (value in api.body.rates) {
                                    list.add(
                                        RatesModel(
                                            title = value.key,
                                            value = "$base/$toKey: ${
                                                String.format(
                                                    "%.2f",
                                                    value.value[toKey]
                                                )
                                            }"
                                        )
                                    )
                                }
                                timeRates.emit(list.sortedByDescending { r ->  r.title })
                            }
                        }
                    }
                    is ApiResource.OnError -> {
                        if (errorCounts < 2){
                            errorState.emit(ErrorState.TRY_AGAIN)
                        }else{
                            errorState.emit(ErrorState.TRY_LATER)
                        }
                        errorCounts++
                    }
                    is ApiResource.OnLoading -> {}
                }
            } }
        }
    }

    suspend fun getCurrency(rateKey: String): CurrencyRate? = db.getCurrencyRateByKey(rateKey)

    suspend fun getPopular(base: String){
        popularRepo.getPopularRates(base)
    }

    suspend fun getRateTimeSeries(
        rate: CurrencyRate,
        timeData: ApiDateTimeModel
    ){
        toKey = rate.to
        timeSeriesRepo.getCurrencyRatesTimeSeries(
            base = rate.base,
            to = rate.to,
            start = timeData.start,
            end = timeData.end
        )
    }


    fun cancelJobs(){
        coroutineJob.cancel()
    }

}