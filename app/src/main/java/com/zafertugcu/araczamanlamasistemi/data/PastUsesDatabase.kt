package com.zafertugcu.araczamanlamasistemi.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.zafertugcu.araczamanlamasistemi.model.PastUsesModel

@Database(entities = [PastUsesModel::class],version = 1,exportSchema = false)
abstract class PastUsesDatabase: RoomDatabase() {

    abstract fun pastUsesDao(): PastUsesDao

    companion object{
        @Volatile
        private var INSTANCE: PastUsesDatabase? = null

        fun getDatabase(context: Context): PastUsesDatabase{
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PastUsesDatabase::class.java,
                    "past_uses_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}