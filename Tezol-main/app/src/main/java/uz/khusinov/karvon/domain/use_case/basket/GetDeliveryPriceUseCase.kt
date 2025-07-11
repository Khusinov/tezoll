package uz.khusinov.karvon.domain.use_case.basket

import android.util.Log
import kotlinx.coroutines.flow.Flow
import uz.khusinov.karvon.domain.model.DeliveryPrice
import uz.khusinov.karvon.domain.model.GetDeliveryPriceRequest
import uz.khusinov.karvon.domain.repository.BasketRepository
import uz.khusinov.karvon.utills.UiStateObject

class GetDeliveryPriceUseCase(
    private val repository: BasketRepository
) {
    operator fun invoke(getDeliveryPriceRequest: GetDeliveryPriceRequest): Flow<UiStateObject<DeliveryPrice>> {
        Log.d("TAG", "invoke: called")
        return repository.getDeliveryPrice(getDeliveryPriceRequest)
    }
}