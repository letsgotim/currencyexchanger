package com.letsgotim.currencyexchanger.room.dao

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import com.letsgotim.currencyexchanger.model.Balance
import com.letsgotim.currencyexchanger.model.Currency
import com.letsgotim.currencyexchanger.model.Settings


@Dao
interface SettingsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertData(data: Settings)

    @Query("SELECT * FROM settings_tbl")
    fun getAllData(): List<Settings>

    @Query("SELECT * FROM settings_tbl")
    fun getData(): Settings

    @Query("SELECT * FROM settings_tbl WHERE id = :id")
    fun getDataById(id: Int): Settings

    @Query("SELECT * FROM settings_tbl WHERE id = :id")
    fun getDataBy(id: Int): Settings

    @Query("DELETE FROM settings_tbl")
    fun deleteAllData()

}

//@PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int,
//@ColumnInfo(name = "balance") val balance: Double?,
//@ColumnInfo(name = "currency") val currency: String?,
//@ColumnInfo(name = "date_entry") val dateEntry: String?