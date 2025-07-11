package uz.khusinov.karvon.domain.use_case.orders

import kotlinx.coroutines.flow.Flow
import uz.khusinov.karvon.domain.model.Order
import uz.khusinov.karvon.domain.repository.OrdersRepository
import uz.khusinov.karvon.utills.UiStateList

class GetOrdersUseCase(
    private val repository: OrdersRepository
) {
    operator fun invoke(): Flow<UiStateList<Order>> {
        return repository.getOrders()
    }
}