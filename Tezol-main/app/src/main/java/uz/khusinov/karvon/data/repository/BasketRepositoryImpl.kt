package uz.khusinov.karvon.data.repository

import FullAddress
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import uz.khusinov.karvon.data.remote.ApiService
import uz.khusinov.karvon.domain.model.CreateOrderRequest
import uz.khusinov.karvon.domain.model.DeliveryPrice
import uz.khusinov.karvon.domain.model.GetDeliveryPriceRequest
import uz.khusinov.karvon.domain.repository.BasketRepository
import uz.khusinov.karvon.utills.userMessage
import uz.khusinov.karvon.utills.UiStateObject

class BasketRepositoryImpl(private val apiService: ApiService) : BasketRepository {

    override fun createOrder(
        userId: String,
        orderRequest: CreateOrderRequest
    ): Flow<UiStateObject<Unit>> = flow {
        emit(UiStateObject.LOADING)
        try {
            val response = apiService.createOrder(orderRequest)
            if (response.success) {
                emit(UiStateObject.SUCCESS(response.data))
            } else {
                emit(UiStateObject.ERROR(response.message))
            }
        } catch (e: Exception) {
            emit(UiStateObject.ERROR(e.userMessage()))
        }
    }

    override fun getAddressNameFromOpenStreet(
        lat: Double,
        long: Double
    ): Flow<UiStateObject<FullAddress>> = flow {
        emit(UiStateObject.LOADING)
        try {
            val response = apiService.getAddressFromLocation(lat, long)
            emit(UiStateObject.SUCCESS(response))
        } catch (e: Exception) {
            emit(UiStateObject.ERROR(e.localizedMessage ?: e.userMessage()))
        }
    }

    override fun getDeliveryPrice(getDeliveryPriceRequest: GetDeliveryPriceRequest): Flow<UiStateObject<DeliveryPrice>> =
        flow {
            emit(UiStateObject.LOADING)
            try {
                val response = apiService.getDeliveryPrice(getDeliveryPriceRequest)
                emit(UiStateObject.SUCCESS(response.data))
            } catch (e: Exception) {
                emit(UiStateObject.ERROR(e.localizedMessage ?: e.userMessage()))
            }
        }
}