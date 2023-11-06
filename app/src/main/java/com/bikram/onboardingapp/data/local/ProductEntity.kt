package com.bikram.onboardingapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ProductEntity(
    @PrimaryKey val id: Int,
    val price: Double,
    val title: String,
    val description: String,
    val category: String,
    val image: String
)