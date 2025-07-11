package uz.khusinov.karvon.data.repository

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import uz.khusinov.karvon.data.remote.ApiService
import uz.khusinov.karvon.domain.model.Data
import uz.khusinov.karvon.domain.model.Order
import uz.khusinov.karvon.domain.model.OrderHistory
import uz.khusinov.karvon.domain.repository.OrdersRepository
import uz.khusinov.karvon.utills.userMessage
import uz.khusinov.karvon.utills.UiStateList
import uz.khusinov.karvon.utills.UiStateObject

class OrdersRepositoryImpl(
    private val apiService: ApiService
) : OrdersRepository {

    override fun getOrders(): Flow<UiStateList<Order>> = flow {
        emit(UiStateList.LOADING)
        try {
            val response = apiService.getOrders()
            if (response.success) {
                emit(UiStateList.SUCCESS(response.data))
            } else {
                emit(UiStateList.ERROR(response.error.message))
            }
        } catch (e: Exception) {
            emit(UiStateList.ERROR(e.userMessage()))
        }
    }

    override fun getOrdersHistory(): Flow<UiStateObject<Data<OrderHistory>>> = flow {
        emit(UiStateObject.LOADING)
        try {
            val response = apiService.getOrdersHistory()
            if (response.success) {
                Log.d("TAG", "getOrdersHistory: if case $response ")
                emit(UiStateObject.SUCCESS(response.data))
            } else {
                Log.d("TAG", "getOrdersHistory: else case $response ")
                emit(UiStateObject.ERROR(response.error.message))
            }
        } catch (e: Exception) {
            Log.d("TAG", "getOrdersHistory: catch ${e.message} ")
            emit(UiStateObject.ERROR(e.userMessage()))
        }
    }
}