package com.faheem.currencyconversion.ui.screens.currencies.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.faheem.currencyconversion.data.models.Rate
import com.faheem.currencyconversion.databinding.LayoutItemExchangeRateBinding
import javax.inject.Inject

class RatesAdapter @Inject constructor() : RecyclerView.Adapter<RatesViewHolder>() {

    private var list: MutableList<Rate> = mutableListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RatesViewHolder {
        return RatesViewHolder(
            LayoutItemExchangeRateBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RatesViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<Rate>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }
}