package com.letsgotim.currencyexchanger.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ConvertViewModelFactory(
    private val context: Context,
    private val currency: String
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ConvertViewModel::class.java)) {
            return ConvertViewModel(context,currency) as T
        }
        throw java.lang.IllegalArgumentException("Unknown View Model Class")
    }

}