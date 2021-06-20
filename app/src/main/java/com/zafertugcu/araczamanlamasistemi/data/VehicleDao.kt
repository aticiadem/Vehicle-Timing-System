package com.zafertugcu.araczamanlamasistemi.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.zafertugcu.araczamanlamasistemi.model.VehicleInfoModel

@Dao
interface VehicleDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addVehicle(vehicle: VehicleInfoModel)

    @Query("SELECT * FROM vehicle_table ORDER BY vehicleId ASC")
    fun readAllData(): LiveData<List<VehicleInfoModel>>

    @Update
    suspend fun updateVehicle(vehicle: VehicleInfoModel)

    @Delete
    suspend fun deleteVehicle(vehicle: VehicleInfoModel)

}