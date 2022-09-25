package com.devwarex.currency.ui.launcher

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.devwarex.currency.R
import com.devwarex.currency.databinding.ActivityLaunchBinding
import com.devwarex.currency.util.NetworkUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LaunchActivity : AppCompatActivity() {
    lateinit var binding:ActivityLaunchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLaunchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val viewModel by viewModels<LaunchViewModel>()
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.updateAppConnectivity(NetworkUtil.isMobileConnectedToInternet(this))
        lifecycleScope.launchWhenStarted {
            launch { viewModel.uiState.collect{
                viewModel.updateUiMessage(
                    when{
                        it.isNetworkAvailable -> getString(R.string.app_connected_message)
                        it.isNetworkLost -> getString(R.string.app_disconnected_message)
                        it.isFetchingData -> getString(R.string.fetching_data_message)
                        it.onSuccess -> getString(R.string.succeed_fetch_message)
                        else -> getString(R.string.empty)
                    }
                )
                if (it.navigate){
                    //TODO Navigate
                }
            } }
        }
    }


    private fun updateUi(){

    }

}