package com.kwh.almuniconnect.network

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "branches")
data class BranchEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val shortName: String
)