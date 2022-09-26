package com.devwarex.currency.ui.home.details

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devwarex.currency.R
import com.devwarex.currency.adapters.RatesAdapter
import com.devwarex.currency.databinding.FragmentDetailsBinding
import com.devwarex.currency.util.ErrorState
import com.devwarex.currency.util.NetworkUtil
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailsFragment : Fragment(
    R.layout.fragment_details
) {

    private val args by navArgs<DetailsFragmentArgs>()
    private val viewModel by viewModels<DetailsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val popularRv = view.findViewById<RecyclerView>(R.id.popular_rate_rv)
        val timeSeriesRv = view.findViewById<RecyclerView>(R.id.time_series_rv)
        val timeSeriesAdapter = RatesAdapter()
        val popularAdapter = RatesAdapter()
        popularRv.adapter = popularAdapter
        timeSeriesRv.adapter = timeSeriesAdapter
        popularRv.layoutManager = LinearLayoutManager(context)
        timeSeriesRv.layoutManager = LinearLayoutManager(context)
        sync()
        lifecycleScope.launchWhenStarted {
            launch {viewModel.popularRates.collect{ popularAdapter.setRates(it) } }
            launch { viewModel.timeRates.collect{ timeSeriesAdapter.setRates(it) } }
            launch { viewModel.errorState.collect{updateUiOnError(it)} }
        }
    }

    private fun sync(){
        val key = args.rateKey
        if (key.isNotEmpty()){
            if (NetworkUtil.isMobileConnectedToInternet(requireActivity())) {
                viewModel.getRate(key)
            }else{
                Toast.makeText(context,"Connect to Internet.",Toast.LENGTH_LONG).show()
            }
        }else{
            Toast.makeText(context,"Something went wrong",Toast.LENGTH_LONG).show()
        }
    }

    private fun updateUiOnError(state: ErrorState){
        when(state){
            ErrorState.TRY_AGAIN -> {
                val view = requireActivity().findViewById<View>(android.R.id.content)
                val snackBar =  Snackbar.make(view,"Something went wrong", Snackbar.LENGTH_LONG)
                snackBar.setAction("Try again") {
                    sync()
                }
                snackBar.setDuration(10000).show()
            }
            ErrorState.TRY_LATER -> {
                val view = requireActivity().findViewById<View>(android.R.id.content)
                val snackBar =  Snackbar.make(view,"Please try again later", Snackbar.LENGTH_LONG)
                snackBar.show()
            }
            else -> {Log.d("error",state.toString())}
        }
    }
}