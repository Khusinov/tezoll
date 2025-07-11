package uz.khusinov.karvon.domain.repository

import kotlinx.coroutines.flow.Flow
import uz.khusinov.karvon.domain.model.Data
import uz.khusinov.karvon.domain.model.Order
import uz.khusinov.karvon.domain.model.OrderHistory
import uz.khusinov.karvon.utills.UiStateList
import uz.khusinov.karvon.utills.UiStateObject

interface OrdersRepository {

    fun getOrders(): Flow<UiStateList<Order>>

    fun getOrdersHistory(): Flow<UiStateObject<Data<OrderHistory>>>

}