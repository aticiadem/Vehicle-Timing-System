package com.zafertugcu.araczamanlamasistemi.data

import androidx.lifecycle.LiveData
import com.zafertugcu.araczamanlamasistemi.model.VehicleInfoModel

class VehicleRepository(private val vehicleDao: VehicleDao) {

    val readAllData: LiveData<List<VehicleInfoModel>> = vehicleDao.readAllData()
//    val readFinishedData: LiveData<List<VehicleInfoModel>> = vehicleDao.readFinishedData()

    suspend fun addVehicle(vehicle: VehicleInfoModel){
        vehicleDao.addVehicle(vehicle)
    }

    suspend fun updateVehicle(vehicle: VehicleInfoModel){
        vehicleDao.updateVehicle(vehicle)
    }

    suspend fun deleteVehicle(vehicle: VehicleInfoModel){
        vehicleDao.deleteVehicle(vehicle)
    }

    suspend fun deleteAllVehicle(){
        vehicleDao.deleteAllVehicle()
    }

}