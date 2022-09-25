package com.devwarex.currency.ui.launcher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devwarex.currency.api.ApiResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LaunchViewModel @Inject constructor(
    private val repo: LaunchRepo
): ViewModel() {

    private val _uiState = MutableStateFlow(LaunchUiState())
    val uiState: StateFlow<LaunchUiState> = _uiState

    init {
        viewModelScope.launch {
            launch { repo.apiState.collectLatest {
                when(val api = it){
                    is ApiResource.OnLoading -> { if (api.isLoading) _uiState.emit(LaunchUiState(isLoading = false, isFetchingData = true)) }
                    is ApiResource.OnSuccess -> {
                        _uiState.emit(_uiState.value.copy(isLoading = false, onSuccess = true, isNetworkAvailable = false))
                        delay(800)
                        _uiState.emit(LaunchUiState(isLoading = false, navigate = true))
                    }
                    is ApiResource.OnError -> _uiState.emit(LaunchUiState(isLoading = false, onError = true))
                }
            } }
        }
    }
    fun updateAppConnectivity(b: Boolean) =  viewModelScope.launch {
        if (_uiState.value.onSuccess || _uiState.value.navigate) return@launch
        _uiState.emit(LaunchUiState(isLoading = false, isNetworkAvailable = b, isNetworkLost = !b))
        if (b){
            repo.sync()
        }
    }

    fun updateUiMessage(id: String){
        viewModelScope.launch {
            _uiState.emit(_uiState.value.copy(message = id))
        }
    }

    override fun onCleared() {
        super.onCleared()
        repo.cancelJobs()
    }
}