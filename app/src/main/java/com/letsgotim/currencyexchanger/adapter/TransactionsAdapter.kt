package com.letsgotim.currencyexchanger.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.letsgotim.currencyexchanger.databinding.ListBalancesBinding
import com.letsgotim.currencyexchanger.databinding.ListCurrencyBinding
import com.letsgotim.currencyexchanger.databinding.ListTransactionsBinding
import com.letsgotim.currencyexchanger.helper.Utility
import com.letsgotim.currencyexchanger.model.Balance
import com.letsgotim.currencyexchanger.model.Currency
import com.letsgotim.currencyexchanger.model.Transaction


class TransactionsAdapter(
    private val context: Context
) :
    RecyclerView.Adapter<TransactionsAdapter.ViewHolder>() {

    private var list: List<Transaction> = ArrayList()

    fun setData(list: List<Transaction>) {
        this.list = list
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ListTransactionsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListTransactionsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.tvAmountSell.text = Utility.getCurrencyFormat(list[position].sellAmount!!)
        holder.binding.tvCurrencySell.text = list[position].baseCurrency

        holder.binding.tvAmountReceive.text =
            "+${Utility.getCurrencyFormat(list[position].convertedAmount!!)}"
        holder.binding.tvCurrencyReceive.text = list[position].currency


//        holder.binding.lltContainer.setOnClickListener {
//            itemClickListener.onItemClicked(list[position])
//        }

    }

    override fun getItemCount(): Int {
        return list.size
    }


}

