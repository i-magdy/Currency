package com.devwarex.currency.ui.home.converter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devwarex.currency.data.DetailsArgs
import com.devwarex.currency.util.ErrorState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConversionViewModel @Inject constructor(
    private val repo: ConverterRepo
): ViewModel() {


    private val _uiState = MutableStateFlow(ConversionUiState())
    private val _navigateState = MutableStateFlow(DetailsArgs("",""))
    val uiState: StateFlow<ConversionUiState> = _uiState
    val currencies get() = repo.currencies
    val navigateState: StateFlow<DetailsArgs> get() = _navigateState
    val errorState: StateFlow<ErrorState> get() = repo.errorState


    init {
        viewModelScope.launch {
            repo.rate.collect {
                if (it.to.isNotEmpty() && it.rate != 0.0 && it.base.isNotBlank()) {
                    _uiState.emit(_uiState.value.copy(
                        enable = it.base.isNotBlank() && it.to.isNotBlank(),
                        from = it.base,
                        to = it.to,
                        rate = it.rate,
                        result = calculateResult(
                            rate = it.rate,
                            amount = _uiState.value.amount),
                        amount = _uiState.value.amount
                    ))
                }
            }
        }
    }
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


    fun onSwitch() = viewModelScope.launch {
        _uiState.emit(
            ConversionUiState(
                from = _uiState.value.to,
                to = _uiState.value.from,
                amount = _uiState.value.result.ifBlank { "1" },
                result = "x"
        ))
    }

    fun onSelectSymbols(from: String,to: String){
        if (from.isBlank() || to.isBlank()) return
        if (from == to) return
        viewModelScope.launch {
            _uiState.emit(_uiState.value.copy(
                enable = false,
                from = from,
                to = to)
            )
            delay(200)
            getRate()
        }
    }

    suspend fun getRate(){
        repo.getRate(
            from = _uiState.value.from,
            to = _uiState.value.to
        )
    }
    fun onAmountChange(s: String){
        if (s.isBlank()){
            viewModelScope.launch { _uiState.emit(_uiState.value.copy(result = "", amount = "")) }
            return
        }
        if (_uiState.value.amount == s || !_uiState.value.enable || s == "x") return
        viewModelScope.launch { _uiState.emit(_uiState.value.copy(
            amount = s,
            result = calculateResult(_uiState.value.rate,s)
        )) }
    }

    fun onResultChange(s: String){
        if (s.isBlank()){
            viewModelScope.launch { _uiState.emit(_uiState.value.copy(result = "", amount = "")) }
            return
        }
        if (_uiState.value.result == s || !_uiState.value.enable) return
        viewModelScope.launch { _uiState.emit(_uiState.value.copy(result = s, amount = calculateAmount(s))) }
    }

    private fun calculateAmount(result: String): String{
        if(_uiState.value.rate == 0.0 || result.isBlank()) return ""
        val s = (1/_uiState.value.rate) * result.toDouble()
        return String.format("%.1f",s)
    }

    private fun calculateResult(rate: Double,amount: String): String{
        if(rate== 0.0 || amount.isBlank()) return ""
        val s = rate * amount.toDouble()
        return String.format("%.1f",s)
    }

    fun navigateToDetails() = viewModelScope.launch {
        _navigateState.emit(DetailsArgs(
            rate_key = "${_uiState.value.from}-${_uiState.value.to}",
            amount = _uiState.value.amount
        ))
    }

    fun removeNavigationObservable() = viewModelScope.launch {
        _navigateState.emit(DetailsArgs("",""))
    }
}
