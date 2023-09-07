package com.bikram.onboardingapp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This data class defines a product which includes an ID, image URL,
 * price, title and description among other few types.
 */
@Serializable
data class Product(
    val id: Int,
    val price: Double,
    val title: String,
    val description: String,
    val category: String,
    @SerialName(value = "image")
    val imgSrc: String
) {
    val priceKr: String by lazy { String.format("%.2f", price * 10) }
}
