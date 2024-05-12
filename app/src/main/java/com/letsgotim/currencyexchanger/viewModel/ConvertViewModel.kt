package com.letsgotim.currencyexchanger.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.letsgotim.currencyexchanger.helper.RetrofitBuilder
import com.letsgotim.currencyexchanger.helper.Utility
import com.letsgotim.currencyexchanger.model.Balance
import com.letsgotim.currencyexchanger.model.Currency
import com.letsgotim.currencyexchanger.model.Submit
import com.letsgotim.currencyexchanger.model.Transaction
import com.letsgotim.currencyexchanger.model.currencyResponse.ApiData
import com.letsgotim.currencyexchanger.network.GetCurrency
import com.letsgotim.currencyexchanger.room.db.Db
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Timer
import java.util.TimerTask

class ConvertViewModel(
    context: Context,
    currency: String,
    baseCurrency: String,
) : ViewModel() {

    private val context: Context
    private val currency: String
    private val baseCurrency: String

    init {
        this.context = context
        this.currency = currency
        this.baseCurrency = baseCurrency
    }

    val displayCurrencyDetails = MutableLiveData<Currency>()
    val displayRemainingBalance = MutableLiveData<Balance>()
    val hasCommissionFee = MutableLiveData<Boolean>()
    val showAlertDialog = MutableLiveData<String>()
    val returnTransactionAmountLimit = MutableLiveData<Double>()

    fun requestCurrencyDetails() {
        CoroutineScope(Dispatchers.IO).launch {
            val data = Db.get(context).getCurrencyDao().getDataByCurrency(currency)

            withContext(Dispatchers.Main) {
                displayCurrencyDetails.value = data
            }
        }
    }

    fun requestRemainingBalance() {
        CoroutineScope(Dispatchers.IO).launch {
            val data = Db.get(context).getBalanceDao().getDataByCurrency("EUR")

            withContext(Dispatchers.Main) {
                displayRemainingBalance.value = data
            }

        }
    }

    fun requestSubmit(submit: Submit) {

        CoroutineScope(Dispatchers.IO).launch {
            val settings = Db.get(context).getSettingsDao().getData()

            var id = 0

            val check = Db.get(context).getTransactionDao().getAllData().size
            if (check == 0) {
                id = 1
            } else {
                id = Db.get(context).getTransactionDao().getLatestData().id + 1
            }

            var finalComFee = 0.0
            if (submit.sellAmount >= settings.freeConversionAmount!!) {
                finalComFee = 0.0
            } else {
                finalComFee = submit.commissionFee
            }

            val data = Transaction(
                id,
                submit.remainingBalance - finalComFee,
                baseCurrency,
                submit.sellAmount,
                currency,
                submit.convertedAmount,
                finalComFee,
                Utility.get24HourDateTime(Utility.getFormatedDateTimeAmPm())!!
            )

            Db.get(context).getTransactionDao().insertData(data)

            val checkExistingCurrency = Db.get(context).getBalanceDao().getDataByCurrency(currency)
            val balId = Db.get(context).getBalanceDao().getLatestData().id + 1

            if (checkExistingCurrency == null) {
                val balData = Balance(
                    balId,
                    data.convertedAmount,
                    currency,
                    Utility.get24HourDateTime(Utility.getFormatedDateTimeAmPm())!!
                )
                Db.get(context).getBalanceDao().insertData(balData)
            } else {
                Db.get(context).getBalanceDao()
                    .updateBalance(
                        checkExistingCurrency.balance!! + data.convertedAmount!!,
                        currency
                    )
            }

            Db.get(context).getBalanceDao()
                .updateBalance(submit.remainingBalance - finalComFee, baseCurrency)

            withContext(Dispatchers.Main) {
                showAlertDialog.value = "You have converted ${data.sellAmount} $baseCurrency to ${
                    Utility.getCurrencyFormat(data.convertedAmount!!)
                } $currency. " +
                        "Commission Fee: ${Utility.getCurrencyFormat(finalComFee)} $baseCurrency"
            }
        }

    }

    fun requestTransactionCount() {
        CoroutineScope(Dispatchers.IO).launch {
            val transactions = Db.get(context).getTransactionDao().getAllData().size
            val settings = Db.get(context).getSettingsDao().getData()

            withContext(Dispatchers.Main) {
                if (transactions >= 5) {
                    hasCommissionFee.value = true

                    val result: Double =
                        settings.freeConversionTransaction!!.toDouble() / (transactions + 1)

                    if (Utility.isWholeNumber(result)) {
                        hasCommissionFee.value = false
                    }
                }

            }
        }
    }

    fun requestTransactionAmountLimit() {
        CoroutineScope(Dispatchers.IO).launch {
            val data = Db.get(context).getSettingsDao().getData()

            withContext(Dispatchers.Main) {
                returnTransactionAmountLimit.value = data.freeConversionAmount
            }
        }
    }

}