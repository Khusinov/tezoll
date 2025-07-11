package uz.khusinov.karvon.domain.use_case.basket

import FullAddress
import android.util.Log
import kotlinx.coroutines.flow.Flow
import uz.khusinov.karvon.domain.repository.BasketRepository
import uz.khusinov.karvon.utills.UiStateObject

class AddressUseCase(
    private val repository: BasketRepository
) {
    operator fun invoke(lat:Double, long:Double): Flow<UiStateObject<FullAddress>> {
        Log.d("TAG", "invoke: called")
        return repository.getAddressNameFromOpenStreet(lat , long)
    }
}