package com.letsgotim.currencyexchanger.room.dao

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import com.letsgotim.currencyexchanger.model.Balance
import com.letsgotim.currencyexchanger.model.Currency


@Dao
interface BalanceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertData(data: Balance)

    @Query("SELECT * FROM balance_tbl")
    fun getAllData(): List<Balance>

    @Query("SELECT * FROM balance_tbl WHERE id = :id")
    fun getDataById(id: Int): Balance

    @Query("SELECT * FROM balance_tbl ORDER BY id DESC LIMIT 1")
    fun getLatestData(): Balance

    @Query("DELETE FROM balance_tbl")
    fun deleteAllData()

    @Query("SELECT * FROM balance_tbl WHERE currency = :currency")
    fun getDataByCurrency(currency: String): Balance

    @Query("UPDATE balance_tbl set balance = :balance WHERE currency = :currency")
    fun updateBalance(balance: Double, currency: String)

}

//@PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int,
//@ColumnInfo(name = "balance") val balance: Double?,
//@ColumnInfo(name = "currency") val currency: String?,
//@ColumnInfo(name = "date_entry") val dateEntry: String?