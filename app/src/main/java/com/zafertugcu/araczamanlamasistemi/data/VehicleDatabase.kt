package com.zafertugcu.araczamanlamasistemi.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.zafertugcu.araczamanlamasistemi.model.VehicleInfoModel

@Database(entities = [VehicleInfoModel::class],version = 1,exportSchema = false)
abstract class VehicleDatabase: RoomDatabase() {

    abstract fun vehicleDao(): VehicleDao

    companion object{
        @Volatile
        private var INSTANCE: VehicleDatabase? = null

        fun getDatabase(context: Context): VehicleDatabase{
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VehicleDatabase::class.java,
                    "vehicle_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}