package com.devwarex.currency.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.devwarex.currency.R
import com.devwarex.currency.models.RatesModel

class RatesAdapter: RecyclerView.Adapter<RatesAdapter.RateViewHolder>() {



    private var rates: List<RatesModel> = arrayListOf()
    inner class RateViewHolder(
        val view: View
    ): RecyclerView.ViewHolder(
        view
    ){

        private val currencyTitle = view.findViewById<TextView>(R.id.rate_title_item)
        private val rateValue = view.findViewById<TextView>(R.id.rate_value_tv)

        fun onBind(rate: RatesModel){
            currencyTitle.text = rate.title
            rateValue.text = rate.value
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RateViewHolder = RateViewHolder(
        view = LayoutInflater.from(parent.context).inflate(R.layout.rate_list_item,parent,false)
    )

    override fun onBindViewHolder(holder: RateViewHolder, position: Int) {
        holder.onBind(rate = rates[position])
    }

    override fun getItemCount(): Int = rates.size


    @SuppressLint("NotifyDataSetChanged")
    fun setRates(rates: List<RatesModel>){
        this.rates = rates
        notifyDataSetChanged()
    }
}