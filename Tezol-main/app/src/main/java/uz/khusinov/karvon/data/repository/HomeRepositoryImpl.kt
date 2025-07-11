package uz.khusinov.karvon.data.repository

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import uz.khusinov.karvon.data.remote.ApiService
import uz.khusinov.karvon.domain.model.AdsResponse
import uz.khusinov.karvon.domain.model.Data
import uz.khusinov.karvon.domain.model.shop.Product
import uz.khusinov.karvon.domain.repository.HomeRepository
import uz.khusinov.karvon.utills.userMessage
import uz.khusinov.karvon.utills.UiStateObject

class HomeRepositoryImpl(
    private val apiService: ApiService,
) : HomeRepository {

    override fun getAds(): Flow<UiStateObject<AdsResponse>> = flow {
        emit(UiStateObject.LOADING)
        try {
            val response = apiService.getAds()
            if (response.success) {
                emit(UiStateObject.SUCCESS(response.data))
            } else {
                emit(UiStateObject.ERROR(response.error.message))
            }
        } catch (e: Exception) {
            Log.d("HomeRepositoryImpl", "getAds: ${e.userMessage()}")
            emit(UiStateObject.ERROR(e.userMessage()))
        }
    }

//    override fun getNewProducts(): Flow<UiStateObject<BasePagination<Product>>> = flow {
//        emit(UiStateObject.LOADING)
//        try {
//            val response = apiService.getNewProducts()
//            if (response.success) {
//                emit(UiStateObject.SUCCESS(response.data))
//            } else {
//                emit(UiStateObject.ERROR(response.error.message))
//            }
//        } catch (e: Exception) {
//            emit(UiStateObject.ERROR(e.userMessage()))
//        }
//    }

    override fun getTopProducts(): Flow<UiStateObject<Data<Product>>> = flow {
        emit(UiStateObject.LOADING)
        try {
            val response = apiService.getTopProducts()
            Log.d("TAG", "getTopProducts: $response ")
            if (response.success) {
                emit(UiStateObject.SUCCESS(response.data))
            } else {
                emit(UiStateObject.ERROR(response.error.message))
            }
        } catch (e: Exception) {
            Log.d("HomeRepositoryImpl", "getTopProducts: ${e.userMessage()}")
            emit(UiStateObject.ERROR(e.userMessage()))
        }
    }

    override fun getMostSoldProducts(): Flow<UiStateObject<Data<Product>>> = flow {
        emit(UiStateObject.LOADING)
        try {
            val response = apiService.getMostSoldProducts()
            if (response.success) {
                emit(UiStateObject.SUCCESS(response.data))
            } else {
                emit(UiStateObject.ERROR(response.error.message))
            }
        } catch (e: Exception) {
            Log.d("HomeRepositoryImpl", "getMostSoldProducts: ${e.userMessage()}")
            emit(UiStateObject.ERROR(e.userMessage()))
        }
    }

    override fun searchProducts(query: String): Flow<UiStateObject<Data<Product>>> = flow {
        emit(UiStateObject.LOADING)
        try {
            val response = apiService.searchProducts(query)
            if (response.success) {
                emit(UiStateObject.SUCCESS(response.data))
            } else {
                emit(UiStateObject.ERROR(response.error.message))
            }
        } catch (e: Exception) {
            Log.d("HomeRepositoryImpl", "search catch: ${e.userMessage()}")
            emit(UiStateObject.ERROR(e.userMessage()))
        }
    }
}