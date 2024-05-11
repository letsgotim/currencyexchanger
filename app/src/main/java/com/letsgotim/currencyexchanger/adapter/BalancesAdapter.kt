package com.letsgotim.currencyexchanger.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.letsgotim.currencyexchanger.databinding.ListBalancesBinding
import com.letsgotim.currencyexchanger.helper.Utility
import com.letsgotim.currencyexchanger.model.Balance


class BalancesAdapter(private val context: Context) :
    RecyclerView.Adapter<BalancesAdapter.ViewHolder>() {

    private var list: List<Balance> = ArrayList()

    fun setData(list: List<Balance>) {
        this.list = list
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ListBalancesBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListBalancesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.tvBalance?.text =
            "" + Utility.getCurrencyFormat(list[position].balance!!) + " " + list[position].currency

    }

    override fun getItemCount(): Int {
        return list.size
    }


}

