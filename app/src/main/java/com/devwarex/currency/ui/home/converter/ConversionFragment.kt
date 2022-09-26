package com.devwarex.currency.ui.home.converter

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.devwarex.currency.R
import com.devwarex.currency.databinding.FragmentConversionBinding
import com.devwarex.currency.util.ErrorState
import com.google.android.material.snackbar.Snackbar
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
        binding.amountEt.addTextChangedListener(amountWatcher)
        binding.resultEt.addTextChangedListener(resultWatcher)
        lifecycleScope.launchWhenStarted {
            launch {viewModel.errorState.collect{updateUiOnError(it)} }
            launch { viewModel.navigateState.collect{
                if (it.amount.isNotEmpty() && it.rate_key.isNotEmpty()){
                    val action = ConversionFragmentDirections.actionNavigateToDetails(
                        rateKey = it.rate_key,
                        amount =it.amount
                    )
                    findNavController().navigate(action)
                }
            } }
        }

    }

    override fun onPause() {
        super.onPause()
        viewModel.removeNavigationObservable()
    }

    private fun updateUiOnError(state: ErrorState){
        when(state){
            ErrorState.TRY_AGAIN -> {
                val view = requireActivity().findViewById<View>(android.R.id.content)
                val snackBar =  Snackbar.make(view,"Something went wrong", Snackbar.LENGTH_LONG)
                snackBar.setAction("Try again") {
                   lifecycleScope.launchWhenStarted { viewModel.getRate() }
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
            val to = binding.toCurrencyAc.editableText.toString()
            val from = binding.fromCurrencyAc.editableText.toString()
            viewModel.onSelectSymbols(
                from = from,
                to = to
            )
            lifecycleScope.launchWhenStarted {
                launch { viewModel.getFromCurrenciesExcept(from).collect{
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
            val to = binding.toCurrencyAc.editableText.toString()
            val from = binding.fromCurrencyAc.editableText.toString()
            viewModel.onSelectSymbols(
                from = from,
                to = to
            )
            lifecycleScope.launchWhenStarted {
              viewModel.getToCurrenciesExcept(to).collect{
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
    private val amountWatcher get() = object : TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            Log.e("text_amount","On Change: $s")
        }

        override fun afterTextChanged(s: Editable?) { viewModel.onAmountChange(s.toString()) }
    }

    private val resultWatcher get() = object : TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            Log.e("text_result","On Change: $s")
        }

        override fun afterTextChanged(s: Editable?) { viewModel.onResultChange(s.toString()) }
    }
}