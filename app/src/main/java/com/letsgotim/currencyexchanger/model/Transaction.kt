package com.letsgotim.currencyexchanger.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaction_tbl")
class Transaction(
    @PrimaryKey (autoGenerate = true) @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "balance") val balance: Double?,
    @ColumnInfo(name = "currency") val currency: String?,
    @ColumnInfo(name = "converted_amount") val convertedAmount: Double?,
    @ColumnInfo(name = "commission_fee") val commissionFee: Double?,
    @ColumnInfo(name = "date_entry") val dateEntry: String?

)

