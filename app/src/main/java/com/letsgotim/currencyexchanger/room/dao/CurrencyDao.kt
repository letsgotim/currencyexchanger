package com.letsgotim.currencyexchanger.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.letsgotim.currencyexchanger.model.Currency


@Dao
interface CurrencyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertData(data: Currency)

    @Query("SELECT * FROM currency_tbl ORDER BY currency")
    fun getAllData(): List<Currency>

    @Query("SELECT * FROM currency_tbl WHERE id = :id")
    fun getDataById(id: Int): Currency


    @Query("DELETE FROM currency_tbl")
    fun deleteAllData()

    @Query("SELECT * FROM currency_tbl WHERE currency = :currency")
    fun getDataByCurrency(currency: String): Currency

}