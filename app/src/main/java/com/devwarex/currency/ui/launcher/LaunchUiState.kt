package com.devwarex.currency.ui.launcher

data class LaunchUiState(
    val isLoading: Boolean = true,
    val isNetworkAvailable: Boolean = false,
    val isNetworkLost: Boolean = false,
    val isFetchingData: Boolean = false,
    val onSuccess: Boolean = false,
    val navigate: Boolean = false,
    val onError: Boolean = false,
    val message: String = ""
)
