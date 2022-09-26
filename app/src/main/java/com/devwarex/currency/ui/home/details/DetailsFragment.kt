package com.devwarex.currency.ui.home.details

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.devwarex.currency.R
import com.devwarex.currency.adapters.RatesAdapter
import com.devwarex.currency.databinding.FragmentDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment(
    R.layout.fragment_details
) {

    private val args by navArgs<DetailsFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentDetailsBinding.inflate(layoutInflater)
        val timeSeriesAdapter = RatesAdapter()
        val popularAdapter = RatesAdapter()
        binding.popularRateRv.adapter = popularAdapter
        binding.timeSeriesRv.adapter = timeSeriesAdapter
        binding.popularRateRv.layoutManager = LinearLayoutManager(context)
        binding.timeSeriesRv.layoutManager = LinearLayoutManager(context)
        val key = args.rateKey
        if (key.isNotEmpty()){

        }
    }
}