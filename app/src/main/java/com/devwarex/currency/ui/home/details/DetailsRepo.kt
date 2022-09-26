package com.devwarex.currency.ui.home.details

import com.devwarex.currency.api.ApiResource
import com.devwarex.currency.db.CurrencyRate
import com.devwarex.currency.db.DatabaseImpl
import com.devwarex.currency.models.RatesModel
import com.devwarex.currency.repos.CurrencyRatePopular
import com.devwarex.currency.repos.CurrencyRateTimeSeries
import com.devwarex.currency.util.ApiDateTimeModel
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
    private val _popularRates = MutableStateFlow<List<RatesModel>>(emptyList())
    private val _timeRates = MutableStateFlow<List<RatesModel>>(emptyList())
    val popularRates: StateFlow<List<RatesModel>> = _popularRates
    val timeRates: StateFlow<List<RatesModel>> = _timeRates

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
                                                value = String.format("$.2f", value.value)
                                            )
                                        )
                                    }
                                    _popularRates.emit(list)
                                }
                            }
                        }
                        is ApiResource.OnError -> {}
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
                                            title = "$base/${value.key}",
                                            value = "$base/$toKey: ${
                                                String.format(
                                                    "$.2f",
                                                    value.value[toKey]
                                                )
                                            }"
                                        )
                                    )
                                }
                                _popularRates.emit(list)
                            }
                        }
                    }
                    is ApiResource.OnError -> {}
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