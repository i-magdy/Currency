package com.devwarex.currency.ui.home.converter

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class ConversionViewModel @Inject constructor(
    private val repo: ConverterRepo
): ViewModel() {


    val currencies get() = repo.currencies

    fun getFromCurrenciesExcept(symbol: String): Flow<List<String>>{
        if (symbol.isNotBlank()) {
             return repo.getCurrenciesExcept(symbol = symbol)
        }
        return flow { emit(emptyList()) }
    }

    fun getToCurrenciesExcept(symbol: String): Flow<List<String>>{
        if (symbol.isNotBlank()) {
            return repo.getCurrenciesExcept(symbol = symbol)
        }
        return flow { emit(emptyList()) }
    }


}
