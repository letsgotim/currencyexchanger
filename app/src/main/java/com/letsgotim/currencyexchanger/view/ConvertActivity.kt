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
            currency
        )


        viewModel = ViewModelProvider(this, viewModelFactory)[ConvertViewModel::class.java]
        viewModel.requestCurrencyDetails()
        viewModel.requestRemainingBalance()

        binding.btnSubmit.isEnabled = false

        binding.etAmount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString() == "") {
                    remainingBalance = balance
                    convertedAmount = 0.0
                    binding.btnSubmit.isEnabled = false
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

                        remainingBalance = balance - s.toString().toDouble()!!
                        convertedAmount = amount * s.toString().toDouble()
                        binding.btnSubmit.isEnabled = true
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

        viewModel.finishActivity.observe(this){
            finish()
        }
    }

}