package com.kwh.almuniconnect.network

import android.util.Log
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import com.google.common.reflect.TypeToken
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class BranchRepository(
    private val dao: BranchDao,
    private val remoteConfig: FirebaseRemoteConfig
) {

    fun getBranches(): Flow<List<Branch>> =
        dao.getBranches().map { list ->
            list.map { it.toDomain() }
        }

    suspend fun fetchAndCacheBranches() {

        remoteConfig.fetchAndActivate().await()

        val json = remoteConfig.getString("branches")

        val branches = parseBranches(json)

        dao.insertAll(branches.map { it.toEntity() })
    }
}


fun parseBranches(json: String?): List<Branch> {

    if (json.isNullOrBlank()) {
        Log.e("BranchParser", "Remote config JSON is empty")
        return emptyList()
    }

    return try {
        val type = object : TypeToken<List<Branch>>() {}.type
        Gson().fromJson<List<Branch>>(json, type) ?: emptyList()
    } catch (e: Exception) {
        Log.e("BranchParser", "JSON parsing error", e)
        emptyList()
    }
}
