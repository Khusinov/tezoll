package uz.khusinov.karvon.domain.use_case.orders

import kotlinx.coroutines.flow.Flow
import uz.khusinov.karvon.domain.model.Data
import uz.khusinov.karvon.domain.model.OrderHistory
import uz.khusinov.karvon.domain.repository.OrdersRepository
import uz.khusinov.karvon.utills.UiStateObject

class GetOrdersHistoryUseCase(
    private val repository: OrdersRepository
) {
    operator fun invoke(): Flow<UiStateObject<Data<OrderHistory>>> {
        return repository.getOrdersHistory()
    }
}