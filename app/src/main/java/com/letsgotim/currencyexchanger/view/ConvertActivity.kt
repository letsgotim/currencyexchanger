package com.letsgotim.currencyexchanger.view

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.letsgotim.currencyexchanger.R
import com.letsgotim.currencyexchanger.databinding.ActivityConvertBinding
import com.letsgotim.currencyexchanger.helper.Utility
import com.letsgotim.currencyexchanger.viewModel.ConvertViewModel
import com.letsgotim.currencyexchanger.viewModel.ConvertViewModelFactory


class ConvertActivity : AppCompatActivity() {

    private lateinit var viewModel: ConvertViewModel
    private lateinit var binding: ActivityConvertBinding
    private lateinit var viewModelFactory: ConvertViewModelFactory

    private var currency = ""
    private var amount = 0.0
    private var convertedAmount = 0.0
    private var balance = 0.0
    private var remainingBalance = 0.0
    private var commissionFee = 0.0
    private var disableSubmit = true
    private var sellAmount = 0.0
    private var hasCommissionFee = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConvertBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.hide()

        val intent = intent
        currency = intent.getStringExtra("CURRENCY")!!
        binding.tvAmount.text = "0.0 $currency"

        init()
        viewModelResponse()
    }

    private lateinit var dialog: Dialog

    private fun init() {
        viewModelFactory = ConvertViewModelFactory(
            application,
            currency,
            "EUR"
        )


        viewModel = ViewModelProvider(this, viewModelFactory)[ConvertViewModel::class.java]
        viewModel.requestCurrencyDetails()
        viewModel.requestRemainingBalance()
        viewModel.requestTransactionCount()

        binding.btnSubmit.isEnabled = false

        binding.etAmount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString() == "") {
                    remainingBalance = balance
                    convertedAmount = 0.0
                    sellAmount = 0.0
                    commissionFee = 0.0
                    binding.btnSubmit.isEnabled = false
                    binding.tvCommisionFee.text = "Commission Fee: 0.0 EUR"
                } else {
                    if (s.toString().toDouble() > balance) {
                        binding.etAmount.setTextColor(
                            ContextCompat.getColor(
                                applicationContext,
                                R.color.pink
                            )
                        )

                        binding.tvAmount.text = "0.0 $currency"
                        binding.btnSubmit.isEnabled = false

                    } else {
                        binding.etAmount.setTextColor(
                            ContextCompat.getColor(
                                applicationContext,
                                R.color.black
                            )
                        )

                        binding.btnSubmit.isEnabled = true

                        sellAmount = s.toString().toDouble()
                        remainingBalance = balance - sellAmount
                        convertedAmount = amount * sellAmount

                        if (hasCommissionFee) {
                            commissionFee = sellAmount * 0.07
                            binding.tvCommisionFee.text =
                                "Commission Fee: ${Utility.getCurrencyFormat(commissionFee)} EUR"
                            remainingBalance -= commissionFee

                            if (remainingBalance <= 0.0){
                                binding.etAmount.setTextColor(
                                    ContextCompat.getColor(
                                        applicationContext,
                                        R.color.pink
                                    )
                                )

                                binding.tvAmount.text = "0.0 $currency"
                                binding.btnSubmit.isEnabled = false
                            }else{
                                binding.etAmount.setTextColor(
                                    ContextCompat.getColor(
                                        applicationContext,
                                        R.color.black
                                    )
                                )

                                binding.btnSubmit.isEnabled = true

                                sellAmount = s.toString().toDouble()
                                remainingBalance = balance - sellAmount
                                remainingBalance -= commissionFee
                                convertedAmount = amount * sellAmount
                            }
                        }
                    }

                }

                binding.tvAmount.text =
                    "" + Utility.getCurrencyFormat(convertedAmount) + " " + currency
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {


            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


            }
        })

        binding.btnSubmit.setOnClickListener {
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setMessage("Confirm currency exchange?")
            alertDialog.setPositiveButton(
                "Yes"
            ) { dialogInterface, _ ->
                viewModel.requestSubmit(
                    sellAmount,
                    remainingBalance,
                    convertedAmount,
                    commissionFee
                )
                dialogInterface.dismiss()
            }
            alertDialog.setNegativeButton(
                "No"
            ) { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            alertDialog.show()
        }


    }


    private fun viewModelResponse() {
        viewModel.displayCurrencyDetails.observe(this) {
            amount = it.amount!!
            binding.tvHeader.text = currency + " " + (it.amount!!)
        }

        viewModel.displayRemainingBalance.observe(this) {
            balance = it.balance!!
            binding.tvRemaining.text =
                "Remaining balance : ${Utility.getCurrencyFormat(it.balance!!)} EUR"
        }



        viewModel.hasCommissionFee.observe(this) {
            hasCommissionFee = it
        }

        viewModel.showAlertDialog.observe(this) {
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle("Currency converted")
            alertDialog.setMessage(it)
            alertDialog.setPositiveButton(
                "Done"
            ) { dialogInterface, _ ->
                finish()
                dialogInterface.dismiss()
            }

            alertDialog.show()
        }
    }


}