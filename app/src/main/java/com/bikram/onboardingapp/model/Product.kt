package com.bikram.onboardingapp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This data class defines a product which includes an ID, image URL,
 * price, title and description among other few types.
 */
@Serializable
data class Product(
    val id: String,
    val price: String,
    val title: String,
    val description: String,
    val category: String,
    @SerialName(value = "image")
    val imgSrc: String
)
