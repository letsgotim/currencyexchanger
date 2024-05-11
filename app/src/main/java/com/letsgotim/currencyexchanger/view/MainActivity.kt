package com.letsgotim.currencyexchanger.view

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.letsgotim.currencyexchanger.R
import com.letsgotim.currencyexchanger.adapter.BalancesAdapter
import com.letsgotim.currencyexchanger.adapter.CurrencyAdapter
import com.letsgotim.currencyexchanger.adapter.TransactionsAdapter
import com.letsgotim.currencyexchanger.databinding.ActivityMainBinding
import com.letsgotim.currencyexchanger.helper.Utility
import com.letsgotim.currencyexchanger.model.Currency
import com.letsgotim.currencyexchanger.viewModel.MainViewModel
import com.letsgotim.currencyexchanger.viewModel.MainViewModelFactory


class MainActivity : AppCompatActivity(), CurrencyAdapter.OnItemClickListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModelFactory: MainViewModelFactory

    private lateinit var balancesAdapter: BalancesAdapter
    private lateinit var currencyAdapter: CurrencyAdapter
    private lateinit var transactionsAdapter: TransactionsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.hide()

        init()
        viewModelResponse()
    }

    private lateinit var dialog: Dialog

    private fun init() {
        viewModelFactory = MainViewModelFactory(
            application
        )

        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        viewModel.getCurrencyApi()
        viewModel.setDefaultSettings()

        binding.btnConvert.isEnabled = false

        binding.rcvBalances.apply {
            layoutManager = LinearLayoutManager(application, LinearLayoutManager.HORIZONTAL, false)
            balancesAdapter = BalancesAdapter(application)
            adapter = balancesAdapter
        }

        binding.rcvTransactions.apply {
            layoutManager = LinearLayoutManager(application)
            transactionsAdapter = TransactionsAdapter(application)
            adapter = transactionsAdapter
        }

        binding.btnConvert.setOnClickListener {
            viewModel.requestCurrency()

        }


    }

    override fun onResume() {
        super.onResume()
        viewModel.requestBalances()
        viewModel.requestTransactions()

        if (!Utility.isNetworkAvailable(application)) {
            Toast.makeText(this, "Please check internet connection", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun viewModelResponse() {
        viewModel.showToastMessage.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        viewModel.displayBalances.observe(this) {
            balancesAdapter.setData(it)
        }

        viewModel.displayCurrencies.observe(this) {
            dialog = Dialog(this@MainActivity)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.dialog_currency)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val rcvCurrency =
                dialog.findViewById<View>(R.id.rcv_currency) as RecyclerView

            rcvCurrency.apply {
                layoutManager = LinearLayoutManager(application)
                currencyAdapter = CurrencyAdapter(application, this@MainActivity)
                addItemDecoration(
                    DividerItemDecoration(
                        application,
                        DividerItemDecoration.VERTICAL
                    )
                )
                adapter = currencyAdapter
            }

            currencyAdapter.setData(it)

            dialog.show()
        }

        viewModel.enableButton.observe(this) {
            binding.btnConvert.isEnabled = it
        }

        viewModel.displayTransactions.observe(this) {
            transactionsAdapter.setData(it)
        }
    }

    /********
     * ONCLICK
     */

    override fun onItemClicked(data: Currency) {
        dialog.dismiss()

        val intent = Intent(this, ConvertActivity::class.java)
        intent.putExtra("CURRENCY", data.currency)
        startActivity(intent)
    }
}