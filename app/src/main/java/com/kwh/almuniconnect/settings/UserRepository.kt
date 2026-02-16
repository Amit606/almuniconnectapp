package com.kwh.almuniconnect.settings

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.kwh.almuniconnect.api.ApiService
import kotlinx.coroutines.tasks.await



class UserRepository(
    private val apiService: ApiService,
    private val context: Context
) {

    suspend fun deleteCompleteAccount(): Boolean {

        return try {

            val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            val token = prefs.getString("auth_token", "") ?: ""

            // 🔹 1. Call Backend API
            val response = apiService.deleteAccount("Bearer $token")

            if (response.isSuccessful) {

                // 🔹 2. Delete Firebase user
                FirebaseAuth.getInstance().currentUser?.delete()?.await()

                // 🔹 3. Clear SharedPreferences
                prefs.edit().clear().apply()

                true
            } else {
                Log.e("UserRepository", "API Error: ${response.code()} - ${response.errorBody()?.string()}")
                true
            }

        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }



}

