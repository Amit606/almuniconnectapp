package com.kwh.almuniconnect.network

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [BranchEntity::class],   // 👈 apni entity yaha add karo
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun branchDao(): BranchDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {

            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "alumni_database"
                ).fallbackToDestructiveMigration()   // 👈 IMPORTANT for now

                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}