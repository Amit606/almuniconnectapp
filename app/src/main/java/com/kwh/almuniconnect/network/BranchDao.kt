package com.kwh.almuniconnect.network

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BranchDao {

    @Query("SELECT * FROM branches")
    fun getBranches(): Flow<List<BranchEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(branches: List<BranchEntity>)
}