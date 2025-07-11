package uz.khusinov.karvon.domain.use_case.basket

import kotlinx.coroutines.flow.Flow
import uz.khusinov.karvon.domain.model.CreateOrderRequest
import uz.khusinov.karvon.domain.repository.BasketRepository
import uz.khusinov.karvon.utills.UiStateObject

class BasketUseCase(
    private val repository: BasketRepository
) {
    operator fun invoke(userId:String, createOrderRequest: CreateOrderRequest): Flow<UiStateObject<Unit>> {
        return repository.createOrder(userId, createOrderRequest)
    }
}