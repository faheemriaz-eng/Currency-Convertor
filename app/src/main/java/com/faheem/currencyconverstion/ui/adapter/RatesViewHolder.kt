package com.faheem.currencyconverstion.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.faheem.currencyconverstion.databinding.LayoutItemExchangeRateBinding
import com.faheem.currencyconverstion.domain.models.Rate

class RatesViewHolder(private val itemBinding: LayoutItemExchangeRateBinding) :
    RecyclerView.ViewHolder(itemBinding.root) {

    fun bind(rate: Rate) {
        with(itemBinding) {
            tvCurrency.text = rate.currencyCode
            tvRate.text = rate.rate.toString()
        }
    }
}
