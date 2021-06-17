package com.zafertugcu.araczamanlamasistemi.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.zafertugcu.araczamanlamasistemi.model.PastUsesModel

@Dao
interface PastUsesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addPastUse(pastUses: PastUsesModel)

    @Query("SELECT * FROM past_uses_table ORDER BY pastId DESC")
    fun readAllData(): LiveData<List<PastUsesModel>>

    @Query("DELETE FROM past_uses_table")
    suspend fun deletePastUses()

}