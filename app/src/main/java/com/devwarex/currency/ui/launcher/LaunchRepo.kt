package com.devwarex.currency.ui.launcher

import android.util.Log
import com.devwarex.currency.api.ApiResource
import com.devwarex.currency.datastore.DatastoreImpl
import com.devwarex.currency.db.DatabaseImpl
import com.devwarex.currency.models.CurrenciesSymbolModel
import com.devwarex.currency.repos.CurrencySymbolsRepo
import com.devwarex.currency.util.TimeoutUtil.isSymbolsTimeout
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import java.util.Calendar
import javax.inject.Inject

class LaunchRepo @Inject constructor(
    private val db: DatabaseImpl,
    private val datastore: DatastoreImpl,
    private val symbolsRepo: CurrencySymbolsRepo
) {

    val apiState = MutableStateFlow<ApiResource<CurrenciesSymbolModel>>(ApiResource.OnLoading(false))
    private val coroutineJob = CoroutineScope(Dispatchers.IO)

    init {
        coroutineJob.launch {
            launch { symbolsRepo.symbols.receiveAsFlow().collect{
                withContext(Dispatchers.Main){
                    when(val resource = it){
                        is ApiResource.OnError ->{ apiState.emit(ApiResource.OnError(error = resource.error, isLoading = resource.isLoading)) }
                        is ApiResource.OnLoading-> { apiState.emit(ApiResource.OnLoading(resource.isLoading)) }
                        is ApiResource.OnSuccess -> {
                            db.insertSymbols(resource.body!!.symbols)
                            apiState.emit(ApiResource.OnSuccess(false,resource.body))
                        }
                    }
                }
            } }
        }
    }

    fun sync(){
        coroutineJob.launch { datastore.symbolTimeout.collectLatest { time ->
            Log.e("time",time.toString())
            if (isSymbolsTimeout(Calendar.getInstance().timeInMillis,time)){
                apiState.emit(ApiResource.OnLoading(isLoading = true))
                symbolsRepo.getAllCurrencies()
                datastore.updateSymbolsTimeout(Calendar.getInstance().timeInMillis)
            }else{
                apiState.emit(ApiResource.OnSuccess(isLoading = false, body = null))
            }
        } }
    }
    fun cancelJobs(){
        coroutineJob.cancel()
    }
}