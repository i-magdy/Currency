package com.devwarex.currency.ui.launcher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LaunchViewModel @Inject constructor(
    private val repo: LaunchRepo
): ViewModel() {


    fun sync() = viewModelScope.launch {  repo.sync() }

}