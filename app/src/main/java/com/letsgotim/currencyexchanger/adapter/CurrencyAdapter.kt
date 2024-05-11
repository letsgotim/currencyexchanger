package com.letsgotim.currencyexchanger.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.letsgotim.currencyexchanger.databinding.ListBalancesBinding
import com.letsgotim.currencyexchanger.databinding.ListCurrencyBinding
import com.letsgotim.currencyexchanger.helper.Utility
import com.letsgotim.currencyexchanger.model.Balance
import com.letsgotim.currencyexchanger.model.Currency


class CurrencyAdapter(
    private val context: Context,
    private val itemClickListener: OnItemClickListener,
    ) :
    RecyclerView.Adapter<CurrencyAdapter.ViewHolder>() {

    private var list: List<Currency> = ArrayList()

    fun setData(list: List<Currency>) {
        this.list = list
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ListCurrencyBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListCurrencyBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.tvCurrency?.text = list[position].currency
        holder.binding.tvAmount?.text = ""+ list[position].amount!!

        holder.binding.lltContainer.setOnClickListener {
            itemClickListener.onItemClicked(list[position])
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    /**********
     * ONCLICK
     */
    interface OnItemClickListener {
        fun onItemClicked(data: Currency)
    }

}

