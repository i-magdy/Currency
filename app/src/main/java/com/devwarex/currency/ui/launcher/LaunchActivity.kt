package com.devwarex.currency.ui.launcher

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.devwarex.currency.R
import com.devwarex.currency.databinding.ActivityLaunchBinding
import com.devwarex.currency.ui.home.MainActivity
import com.devwarex.currency.util.NetworkUtil
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LaunchActivity : AppCompatActivity() {
    private lateinit var binding:ActivityLaunchBinding
    private val viewModel by viewModels<LaunchViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLaunchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.updateAppConnectivity(NetworkUtil.isMobileConnectedToInternet(this))
        lifecycleScope.launchWhenStarted {
            launch { viewModel.uiState.collect{
                Log.e("state",Gson().toJson(it))
                viewModel.updateUiMessage(
                    when{
                        it.isNetworkAvailable -> getString(R.string.app_connected_message)
                        it.isNetworkLost -> getString(R.string.app_disconnected_message)
                        it.isFetchingData -> getString(R.string.fetching_data_message)
                        it.onSuccess -> getString(R.string.succeed_fetch_message)
                        it.onError -> "Something went wrong!."
                        else -> getString(R.string.empty)
                    }
                )
                if (it.navigate){
                    updateUi()
                }
            } }
        }
    }


    private fun updateUi(){
        val homeIntent = Intent(this@LaunchActivity,MainActivity::class.java)
        viewModel.cancelJobs()
        startActivity(homeIntent)
        finish()
    }

}