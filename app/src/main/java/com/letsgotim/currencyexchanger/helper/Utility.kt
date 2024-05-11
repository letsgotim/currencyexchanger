@file:Suppress("DEPRECATION")

package com.letsgotim.currencyexchanger.helper

import android.content.Context
import android.icu.number.IntegerWidth
import android.net.ConnectivityManager
import android.util.Log
import java.text.*
import java.util.*

object Utility {
    fun isNetworkAvailable(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting
    }

    fun getCurrencyFormat(value: Double): String {
        val nf = NumberFormat.getCurrencyInstance()
        val decimalFormatSymbols = (nf as DecimalFormat).decimalFormatSymbols
        decimalFormatSymbols.currencySymbol = ""
        nf.decimalFormatSymbols = decimalFormatSymbols
        return nf.format(value).trim { it <= ' ' }
    }


    fun get24HourDateTime(dateStr: String?): String? {
        val readFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa")
        val writeFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var date: Date? = null
        try {
            date = readFormat.parse(dateStr)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        var formattedDate: String? = ""
        if (date != null) {
            formattedDate = writeFormat.format(date)
        }
        return formattedDate
    }

    fun getFormatedDateTimeAmPm(): String? {
        val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa")
        return sdf.format(Date())
    }

}