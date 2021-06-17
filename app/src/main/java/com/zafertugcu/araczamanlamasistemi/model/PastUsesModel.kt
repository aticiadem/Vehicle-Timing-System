package com.zafertugcu.araczamanlamasistemi.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="past_uses_table")
data class PastUsesModel(
    @PrimaryKey(autoGenerate = true)
    val pastId: Int,
    val pastDate: String,
    val pastVehicleName: String,
    val pastState: Int
)
