package com.letsgotim.currencyexchanger.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.letsgotim.currencyexchanger.model.Balance
import com.letsgotim.currencyexchanger.model.Currency
import com.letsgotim.currencyexchanger.model.Transaction


@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertData(data: Transaction)

    @Query("SELECT * FROM transaction_tbl")
    fun getAllData(): List<Transaction>

    @Query("SELECT * FROM transaction_tbl WHERE id = :id")
    fun getDataById(id: Int): Transaction

    @Query("SELECT * FROM transaction_tbl ORDER BY id DESC LIMIT 1")
    fun getLatestData(): Transaction

    @Query("DELETE FROM transaction_tbl")
    fun deleteAllData()

    @Query("SELECT * FROM transaction_tbl WHERE currency = :currency")
    fun getDataByCurrency(currency: String): Transaction

}