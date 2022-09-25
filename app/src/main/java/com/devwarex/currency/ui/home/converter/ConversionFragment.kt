package com.devwarex.currency.ui.home.converter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.devwarex.currency.R
import com.devwarex.currency.databinding.FragmentConversionBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ConversionFragment : Fragment(
    R.layout.fragment_conversion
) {

    private lateinit var binding: FragmentConversionBinding
    private val viewModel by activityViewModels<ConversionViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConversionBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = requireActivity()
        updateSymbols()

    }


    private fun updateSymbols(){
        lifecycleScope.launchWhenStarted {
            launch {
                viewModel.currencies.collect {
                    val adapter = ArrayAdapter<String>(
                        context!!,
                        android.R.layout.simple_dropdown_item_1line,
                        it
                    )
                    binding.fromCurrencyAc.setAdapter(adapter)
                    binding.toCurrencyAc.setAdapter(adapter)
                }
            }
        }
        binding.fromCurrencyAc.doAfterTextChanged {
            lifecycleScope.launchWhenStarted {
                launch { viewModel.getFromCurrenciesExcept(binding.fromCurrencyAc.editableText.toString()).collect{
                    val adapter = ArrayAdapter<String>(
                        context!!,
                        android.R.layout.simple_dropdown_item_1line,
                        it
                    )
                    binding.toCurrencyAc.setAdapter(adapter)
                } }
            }
        }

        binding.toCurrencyAc.doAfterTextChanged {
            lifecycleScope.launchWhenStarted {
              viewModel.getToCurrenciesExcept(binding.toCurrencyAc.editableText.toString()).collect{
                  val adapter = ArrayAdapter<String>(
                      context!!,
                      android.R.layout.simple_dropdown_item_1line,
                      it
                  )
                  binding.fromCurrencyAc.setAdapter(adapter)
              }
            }
        }
    }
}