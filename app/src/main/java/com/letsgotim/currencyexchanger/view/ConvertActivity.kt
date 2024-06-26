package com.letsgotim.currencyexchanger.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.letsgotim.currencyexchanger.R
import com.letsgotim.currencyexchanger.databinding.ActivityConvertBinding
import com.letsgotim.currencyexchanger.helper.Utility
import com.letsgotim.currencyexchanger.model.Submit
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
    private var transactionAmountLimit = 0.0

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
        viewModel.requestTransactionAmountLimit()

        binding.btnSubmit.isEnabled = false

        binding.etAmount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString() == "") {
                    remainingBalance = balance
                    convertedAmount = 0.0
                    sellAmount = 0.0
                    commissionFee = 0.0

                    setTextCommission("Commission Fee: 0.0 EUR")
                    enableSubmitButton(false)

                } else {
                    sellAmount = s.toString().toDouble()

                    if (sellAmount > balance) {
                        changeEditTextColor(false)
                        enableSubmitButton(false)
                        setTextAmount("0.0 $currency")

                        binding.btnSubmit.isEnabled = false

                    } else {
                        changeEditTextColor(true)
                        enableSubmitButton(true)

                        remainingBalance = balance - sellAmount
                        convertedAmount = amount * sellAmount

                        if (hasCommissionFee) {
                            commissionFee = sellAmount * 0.007

                            setTextCommission(
                                "Commission Fee: ${
                                    Utility.getCurrencyFormat(
                                        commissionFee
                                    )
                                } EUR"
                            )

                            if (remainingBalance <= 0.0) {
                                changeEditTextColor(false)
                                enableSubmitButton(false)
                                setTextAmount("0.0 $currency")

                            } else {
                                changeEditTextColor(true)
                                enableSubmitButton(true)

                                remainingBalance = balance - sellAmount
                                convertedAmount = amount * sellAmount
                            }

                            if (sellAmount >= transactionAmountLimit){
                                setTextCommission("Commission Fee: 0.0 EUR")
                            }
                        } else {
                            setTextCommission("Commission Fee: 0.0 EUR")
                        }
                    }

                }

                setTextAmount("" + Utility.getCurrencyFormat(convertedAmount) + " " + currency)
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
                val data = Submit(
                    sellAmount,
                    remainingBalance,
                    convertedAmount,
                    commissionFee
                )
                viewModel.requestSubmit(
                    data
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

    private fun setTextCommission(str: String) {
        binding.tvCommisionFee.text = str
    }

    private fun setTextAmount(str: String) {
        binding.tvAmount.text = str
    }

    private fun enableSubmitButton(bool: Boolean) {
        binding.btnSubmit.isEnabled = bool
    }

    private fun changeEditTextColor(bool: Boolean) {
        if (bool) {
            binding.etAmount.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.black
                )
            )
        } else {
            binding.etAmount.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.pink
                )
            )
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
            alertDialog.setCancelable(false)
            alertDialog.setMessage(it)
            alertDialog.setPositiveButton(
                "Done"
            ) { dialogInterface, _ ->
                finish()
                dialogInterface.dismiss()
            }

            alertDialog.show()
        }
        viewModel.returnTransactionAmountLimit.observe(this){
            transactionAmountLimit = it
        }
    }


}