package com.devwarex.currency.ui.home.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devwarex.currency.models.RatesModel
import com.devwarex.currency.util.ApiTimeUtil
import com.devwarex.currency.util.ErrorState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repo: DetailsRepo
): ViewModel() {


    val popularRates: StateFlow<List<RatesModel>> = repo.popularRates
    val timeRates: StateFlow<List<RatesModel>> = repo.timeRates
    val errorState: StateFlow<ErrorState> get() = repo.errorState


    fun getRate(key: String){
        viewModelScope.launch {
            val rate = repo.getCurrency(rateKey = key)
            if (rate != null){
                repo.getPopular(rate.base)
                repo.getRateTimeSeries(rate, timeData = ApiTimeUtil.getTimeSeries(3))
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        repo.cancelJobs()
    }
}