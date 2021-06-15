package com.zafertugcu.araczamanlamasistemi.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vehicle_table")
data class VehicleInfoModel(
    @PrimaryKey(autoGenerate = true)
    val vehicleId: Int,
    var vehicleNumber: Int,
    var vehicleTime: Int
)
