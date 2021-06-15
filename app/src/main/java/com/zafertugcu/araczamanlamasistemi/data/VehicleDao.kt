package com.zafertugcu.araczamanlamasistemi.data

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zafertugcu.araczamanlamasistemi.model.VehicleInfoModel

interface VehicleDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addVehicle(vehicle: VehicleInfoModel)

    @Query("SELECT * FROM vehicle_table ORDER BY vehicleId ASC")
    fun readAllData(): LiveData<List<VehicleInfoModel>>

}