package com.devwarex.currency.ui.home.converter

import android.util.Log
import com.devwarex.currency.api.ApiResource
import com.devwarex.currency.db.CurrencyRate
import com.devwarex.currency.db.DatabaseImpl
import com.devwarex.currency.repos.CurrencyConversionRepo
import com.devwarex.currency.util.ErrorState
import com.devwarex.currency.util.TimeoutUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ConverterRepo @Inject constructor(
    private val db: DatabaseImpl,
    private val conversionRepo: CurrencyConversionRepo
) {

    private val _rate = MutableStateFlow<CurrencyRate>(CurrencyRate("","","",0.0,0L))
    val errorState = MutableStateFlow(ErrorState.NONE)
    val rate: StateFlow<CurrencyRate> = _rate
    val currencies get() = db.currencies
    private val coroutineJob = CoroutineScope(Dispatchers.IO)
    private var errorCounts = 0

    init {
        coroutineJob.launch {
            launch {
                conversionRepo.rate.receiveAsFlow().collect{
                    when(val api = it){
                        is ApiResource.OnSuccess -> db.insertRate(api.body)
                        is ApiResource.OnError -> {
                            Log.e("error_$errorCounts", api.error)
                            if (errorCounts < 3){
                                errorState.emit(ErrorState.TRY_AGAIN)
                            }else{
                                errorState.emit(ErrorState.TRY_LATER)
                            }
                            errorCounts++
                        }
                        else-> {}
                }
            }
            }
        }
    }

    fun getCurrenciesExcept(symbol: String) = db.getCurrenciesExcept(symbol = symbol)

    suspend fun getRate(from: String,to: String){
         db.getCurrencyRate(
             rateKey = "$from-$to",
             time = TimeoutUtil.rateTime()
         ).collect{
             if (it == null){
                 conversionRepo.convertCurrency(
                     from = from,
                     to = to
                 )
             }else{
                 _rate.emit(it)
             }
         }
    }


}