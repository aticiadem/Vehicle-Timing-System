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

    /*@Query("SELECT * FROM vehicle_table WHERE vehicleAdded == :added AND vehicleTime == :time AND vehicleIsFinished == :isFinished")
    fun readFinishedData(
        added: Boolean = false,
        time: Int = 0,
        isFinished: Int = 2
    ): LiveData<List<VehicleInfoModel>>*/

    @Update
    suspend fun updateVehicle(vehicle: VehicleInfoModel)

    @Delete
    suspend fun deleteVehicle(vehicle: VehicleInfoModel)

    @Query("DELETE FROM vehicle_table")
    suspend fun deleteAllVehicle()

}