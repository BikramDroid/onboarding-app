package com.bikram.onboardingapp.domain.model

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
    val image: String
) {
    val priceKr: String by lazy { String.format("%.2f", price * 10) }

    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingCombinations = listOf(
            title,
            category,
            description,
            "$id",
            "$price",
        )

        return matchingCombinations.any {
            it.contains(query, ignoreCase = true)
        }
    }

    fun doesMatchSearchCustomQuery(query: String): Boolean {
        var updatedQuery = query
        if (updatedQuery.contains("Jewellery"))     //Tomato tomato -- GB/EN confusions messed up
            updatedQuery = "Jewelery"
        else if (updatedQuery.contains("Computer"))
            updatedQuery = "monitor"

        val matchingCombinations = listOf(
            title,
            category
        )

        return matchingCombinations.any {
            it.contains(updatedQuery, ignoreCase = true)
        }
    }
}
