package com.letsgotim.currencyexchanger.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "balance_tbl")
class Balance(
    @PrimaryKey (autoGenerate = true) @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "balance") val balance: Double?,
    @ColumnInfo(name = "currency") val currency: String?,
    @ColumnInfo(name = "date_entry") val dateEntry: String?

)

