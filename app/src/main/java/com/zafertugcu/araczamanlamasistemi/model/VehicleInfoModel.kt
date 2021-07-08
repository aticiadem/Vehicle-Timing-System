package com.zafertugcu.araczamanlamasistemi.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vehicle_table")
data class VehicleInfoModel(
    @PrimaryKey(autoGenerate = true)
    val vehicleId: Int,
    var vehicleName: String,
    var vehicleMainTime: Int,
    var vehicleTime: Int,
    var vehicleColor: String,
    var vehicleIsStarted: Boolean = false,
    var vehicleIsFinished: Int = 2,
    var vehicleFinishText: String,
    var vehicleAdded: Boolean = false,
)