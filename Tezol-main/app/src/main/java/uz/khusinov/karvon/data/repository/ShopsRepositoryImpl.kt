package uz.khusinov.karvon.data.repository

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import uz.khusinov.karvon.data.remote.ApiService
import uz.khusinov.karvon.domain.model.base.BasePagination
import uz.khusinov.karvon.domain.model.shop.Product
import uz.khusinov.karvon.domain.repository.ShopsRepository
import uz.khusinov.karvon.utills.userMessage
import uz.khusinov.karvon.utills.UiStateObject

class ShopsRepositoryImpl(
    private val apiService: ApiService
) : ShopsRepository {

    override fun getShopsProducts(shopId: Int): Flow<UiStateObject<BasePagination<Product>>> =
        flow {
            emit(UiStateObject.LOADING)
            try {
                val response = apiService.getShopsProducts(shopId)
                if (response.success) {
                    Log.d("TXA", "getShopsProducts successData = ${response.data} ")
                    emit(UiStateObject.SUCCESS(response.data))
                } else {
                    Log.d("TXA", "getShopsProducts errorMessage = ${response.message} ")
                    emit(UiStateObject.ERROR(response.error.message))
                }
            } catch (e: Exception) {
                Log.d("TXA", "getShopsProducts exceptionMessage = ${e.message} ")
                emit(UiStateObject.ERROR(e.userMessage()))
            }
        }

    override fun getCategoryProducts(categoryId: Int): Flow<UiStateObject<BasePagination<Product>>> =
        flow {
            emit(UiStateObject.LOADING)
            try {
                val response = apiService.getCategoryProducts(categoryId)
                if (response.success) {
                    Log.d("TXA", "getCategoryProducts successData = ${response.data} ")
                    emit(UiStateObject.SUCCESS(response.data))
                } else {
                    Log.d("TXA", "getCategoryProducts errorMessage = ${response.message} ")
                    emit(UiStateObject.ERROR(response.error.message))
                }
            } catch (e: Exception) {
                Log.d("TXA", "getCategoryProducts exceptionMessage = ${e.message} ")
                emit(UiStateObject.ERROR(e.userMessage()))
            }
        }

//    override fun getOrderDetail(id: Int): Flow<UiStateObject<Order>> = flow {
//        emit(UiStateObject.LOADING)
//        try {
//            val response = apiService.getOrderDetail(id)
//            if (response.success) {
//                emit(UiStateObject.SUCCESS(response.result))
//            } else {
//                emit(UiStateObject.ERROR(response.error.message))
//            }
//        } catch (e: Exception) {
//            emit(UiStateObject.ERROR(e.localizedMessage ?: e.userMessage()))
//        }
//    }

//    override fun acceptOrder(orderId: Int): Flow<UiStateObject<Order>> = flow {
//        emit(UiStateObject.LOADING)
//        try {
//            val response = apiService.acceptOrder(orderId)
//            if (response.success) {
//                emit(UiStateObject.SUCCESS(response.result))
//            } else {
//                emit(UiStateObject.ERROR(response.error.message))
//            }
//        } catch (e: Exception) {
//            emit(UiStateObject.ERROR(e.localizedMessage ?: e.userMessage()))
//        }
//    }

//    override fun arrive(orderId: Int): Flow<UiStateObject<Order>> = flow {
//        emit(UiStateObject.LOADING)
//        try {
//            val response = apiService.arrive(orderId)
//            if (response.success) {
//                emit(UiStateObject.SUCCESS(response.result))
//            } else {
//                emit(UiStateObject.ERROR(response.error.message))
//            }
//        } catch (e: Exception) {
//            emit(UiStateObject.ERROR(e.localizedMessage ?: e.userMessage()))
//        }
//    }

//    override fun startOrder(orderId: Int): Flow<UiStateObject<Order>> = flow {
//        emit(UiStateObject.LOADING)
//        try {
//            val response = apiService.startOrder(orderId)
//            if (response.success) {
//                emit(UiStateObject.SUCCESS(response.result))
//            } else {
//                emit(UiStateObject.ERROR(response.error.message))
//            }
//        } catch (e: Exception) {
//            emit(UiStateObject.ERROR(e.localizedMessage ?: e.userMessage()))
//        }
//    }

//    override fun completeOrder(orderId: Int): Flow<UiStateObject<Order>> = flow {
//        emit(UiStateObject.LOADING)
//        try {
//            val response = apiService.completeOrder(orderId)
//            if (response.success) {
//                emit(UiStateObject.SUCCESS(response.result))
//            } else {
//                emit(UiStateObject.ERROR(response.error.message))
//            }
//        } catch (e: Exception) {
//            emit(UiStateObject.ERROR(e.localizedMessage ?: e.userMessage()))
//        }
//    }

//    override fun getMyOrdersHistory(): Flow<UiStateObject<Data<OrderHistory>>> = flow {
//        emit(UiStateObject.LOADING)
//        try {
//            val response = apiService.getMyOrdersHistory(1)
//            if (response.success) {
//                emit(UiStateObject.SUCCESS(response.result))
//            } else {
//                emit(UiStateObject.ERROR(response.error.message))
//            }
//        } catch (e: Exception) {
//            emit(UiStateObject.ERROR(e.localizedMessage ?: e.userMessage()))
//        }
//    }
}