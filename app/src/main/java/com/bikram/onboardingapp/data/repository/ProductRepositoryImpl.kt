package com.bikram.onboardingapp.data.repository

import com.bikram.onboardingapp.data.local.ProductDatabase
import com.bikram.onboardingapp.data.mapper.toProduct
import com.bikram.onboardingapp.data.mapper.toProductEntity
import com.bikram.onboardingapp.data.remote.ProductApi
import com.bikram.onboardingapp.domain.model.Product
import com.bikram.onboardingapp.domain.repository.ProductRepository
import com.bikram.onboardingapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepositoryImpl @Inject constructor(
    private val api: ProductApi,
    db: ProductDatabase
) : ProductRepository {

    private val dao = db.dao

    override suspend fun getProducts(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<Product>>> {
        return flow {
            emit(Resource.Loading(true))
            val localProducts = dao.searchProducts(query)
            emit(Resource.Success(
                data = localProducts.map { it.toProduct() }
            ))

            val isDbEmpty = localProducts.isEmpty() && query.isBlank()
            val loadFromCache = !isDbEmpty && !fetchFromRemote
            if (loadFromCache) {
                emit(Resource.Loading(false))
                return@flow
            }
            
            val remoteProducts = try {
                api.getProducts()
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                null
            }

            remoteProducts?.let { listings ->
                dao.clearProducts()
                dao.insertProducts(
                    listings.map { it.toProductEntity() }
                )
                emit(Resource.Success(
                    data = dao
                        .searchProducts("")
                        .map { it.toProduct() }
                ))
                emit(Resource.Loading(false))
            }
        }
    }
}