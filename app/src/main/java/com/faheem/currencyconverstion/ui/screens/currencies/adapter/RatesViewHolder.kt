package com.faheem.currencyconverstion.ui.screens.currencies.adapter

import androidx.recyclerview.widget.RecyclerView
import com.faheem.currencyconverstion.data.models.Rate
import com.faheem.currencyconverstion.databinding.LayoutItemExchangeRateBinding

class RatesViewHolder(private val itemBinding: LayoutItemExchangeRateBinding) :
    RecyclerView.ViewHolder(itemBinding.root) {

    fun bind(rate: Rate) {
        with(itemBinding) {
            tvCurrency.text = rate.currencyCode
            tvRate.text = rate.rate.toString()
        }
    }
}
