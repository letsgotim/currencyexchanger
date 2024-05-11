package com.letsgotim.currencyexchanger.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings_tbl")
class Settings(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "free_conversion_transaction") val freeConversionTransaction: Int?,
    @ColumnInfo(name = "free_conversion_amount") val freeConversionAmount: Double?

)

