package com.bikram.onboardingapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(
        productEntities: List<ProductEntity>
    )

    @Query("DELETE FROM ProductEntity")
    suspend fun clearProducts()

    @Query(
        """
            SELECT * 
            FROM ProductEntity
            WHERE LOWER(title) LIKE '%' || LOWER(:query) || '%'
        """
    )
    suspend fun searchProducts(query: String): List<ProductEntity>
}