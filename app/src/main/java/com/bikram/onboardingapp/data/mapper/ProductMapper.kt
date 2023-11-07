package com.bikram.onboardingapp.data.mapper

import com.bikram.onboardingapp.data.local.ProductEntity
import com.bikram.onboardingapp.domain.model.Product

fun ProductEntity.toProduct(): Product {
    return Product(
        id = id,
        price = price,
        title = title,
        description = description,
        category = category,
        image = image
    )
}

fun Product.toProductEntity(): ProductEntity {
    return ProductEntity(
        id = id,
        price = price,
        title = title,
        description = description,
        category = category,
        image = image
    )
}