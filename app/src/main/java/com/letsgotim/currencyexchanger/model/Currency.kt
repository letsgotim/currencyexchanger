package com.letsgotim.currencyexchanger.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency_tbl")
class Currency(
    @PrimaryKey (autoGenerate = true) @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "base") val base: String?,
    @ColumnInfo(name = "date") val date: String?,
    @ColumnInfo(name = "currency") val currency: String?,
    @ColumnInfo(name = "amount") val amount: Double?
)

