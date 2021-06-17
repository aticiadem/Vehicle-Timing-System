package com.zafertugcu.araczamanlamasistemi.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.zafertugcu.araczamanlamasistemi.data.PastUsesDatabase
import com.zafertugcu.araczamanlamasistemi.data.PastUsesRepository
import com.zafertugcu.araczamanlamasistemi.model.PastUsesModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PastUsesViewModel(application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<PastUsesModel>>
    private val repository: PastUsesRepository

    init {
        val pastUsesDao = PastUsesDatabase.getDatabase(application).pastUsesDao()
        repository = PastUsesRepository(pastUsesDao)
        readAllData = repository.readAllData
    }

    fun addPastUse(pastUse: PastUsesModel){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addPastUse(pastUse)
        }
    }

}