package com.letsgotim.currencyexchanger.room.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.letsgotim.currencyexchanger.model.Balance
import com.letsgotim.currencyexchanger.model.Currency
import com.letsgotim.currencyexchanger.model.Transaction
import com.letsgotim.currencyexchanger.room.dao.BalanceDao
import com.letsgotim.currencyexchanger.room.dao.CurrencyDao
import com.letsgotim.currencyexchanger.room.dao.TransactionDao


@Database(
    version = 1,
    entities = [
        Currency::class,
        Balance::class,
        Transaction::class
    ],
    exportSchema = true
)

abstract class Db : RoomDatabase() {

    abstract fun getCurrencyDao(): CurrencyDao
    abstract fun getBalanceDao(): BalanceDao
    abstract fun getTransactionDao(): TransactionDao

    companion object {
        @Volatile
        private var INSTANCE: Db? = null

        fun get(context: Context): Db {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    Db::class.java,
                    "db"
                )
                    .build()
                INSTANCE = instance
                return instance
            }
        }

    }


}