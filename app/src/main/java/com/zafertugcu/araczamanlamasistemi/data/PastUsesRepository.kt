package com.zafertugcu.araczamanlamasistemi.data

import androidx.lifecycle.LiveData
import com.zafertugcu.araczamanlamasistemi.model.PastUsesModel

class PastUsesRepository(private val pastUsesDao: PastUsesDao) {

    val readAllData: LiveData<List<PastUsesModel>> = pastUsesDao.readAllData()

    suspend fun addPastUse(pastUses: PastUsesModel){
        pastUsesDao.addPastUse(pastUses)
    }

}