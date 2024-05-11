package com.letsgotim.currencyexchanger.viewModel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.letsgotim.currencyexchanger.helper.RetrofitBuilder
import com.letsgotim.currencyexchanger.helper.Utility
import com.letsgotim.currencyexchanger.model.Balance
import com.letsgotim.currencyexchanger.model.Currency
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

class MainViewModel(context: Context) : ViewModel() {

    private val context: Context

    init {
        this.context = context
    }

    val showToastMessage = MutableLiveData<String>()
    val displayBalances = MutableLiveData<List<Balance>>()
    val displayCurrencies = MutableLiveData<List<Currency>>()
    val displayTransactions = MutableLiveData<List<Transaction>>()
    val enableButton = MutableLiveData<Boolean>()

    fun getCurrencyApi() {

        CoroutineScope(Dispatchers.IO).launch {
            val timer = Timer()

            timer.schedule(object : TimerTask() {
                override fun run() {
                    val service = RetrofitBuilder.buildService(GetCurrency::class.java)
                    service.getData()
                        .enqueue(object : Callback<ApiData> {
                            override fun onResponse(
                                call: Call<ApiData>,
                                response: Response<ApiData>
                            ) {

                                val base = response.body()!!.base
                                val date = response.body()!!.date
                                var counter = 0;

                                for (i in response.body()!!.rates) {
                                    counter++
                                    val data = Currency(
                                        counter,
                                        base,
                                        date,
                                        i.key,
                                        i.value
                                    )
                                    CoroutineScope(Dispatchers.IO).launch {
                                        Db.get(context).getCurrencyDao().insertData(data)

                                        withContext(Dispatchers.Main){
                                            enableButton.value = true
                                        }
                                    }

                                }
                            }

                            override fun onFailure(call: Call<ApiData>, t: Throwable) {
                                timer.cancel()
                                showToastMessage.value = "Unable to connect to server"
                            }

                        })
                }
            }, 0, 5000)

        }

    }


    fun requestBalances() {
        CoroutineScope(Dispatchers.IO).launch {

            val check = Db.get(context).getBalanceDao().getAllData().isEmpty()
            if (check) {
                val data = Balance(
                    1,
                    1000.0,
                    "EUR",
                    Utility.get24HourDateTime(Utility.getFormatedDateTimeAmPm())!!
                )
                Db.get(context).getBalanceDao().insertData(data)
            }

            val data = Db.get(context).getBalanceDao().getAllData()

            withContext(Dispatchers.Main) {
                displayBalances.value = data
            }
        }
    }

    fun requestCurrency() {
        CoroutineScope(Dispatchers.IO).launch {
            val data = Db.get(context).getCurrencyDao().getAllData()

            withContext(Dispatchers.Main) {
                displayCurrencies.value = data
            }
        }
    }

    fun requestTransactions() {
        CoroutineScope(Dispatchers.IO).launch {
            val data = Db.get(context).getTransactionDao().getAllData()

            withContext(Dispatchers.Main){
                displayTransactions.value=data
            }
        }
    }


}