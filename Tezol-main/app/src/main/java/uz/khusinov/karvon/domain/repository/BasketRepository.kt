package uz.khusinov.karvon.domain.repository

import FullAddress
import kotlinx.coroutines.flow.Flow
import uz.khusinov.karvon.domain.model.CreateOrderRequest
import uz.khusinov.karvon.domain.model.DeliveryPrice
import uz.khusinov.karvon.domain.model.GetDeliveryPriceRequest
import uz.khusinov.karvon.utills.UiStateObject


interface BasketRepository {
    fun createOrder(userId: String, orderRequest: CreateOrderRequest): Flow<UiStateObject<Unit>>

    fun getAddressNameFromOpenStreet(lat: Double, long: Double): Flow<UiStateObject<FullAddress>>

    fun getDeliveryPrice(getDeliveryPriceRequest: GetDeliveryPriceRequest): Flow<UiStateObject<DeliveryPrice>>
}

