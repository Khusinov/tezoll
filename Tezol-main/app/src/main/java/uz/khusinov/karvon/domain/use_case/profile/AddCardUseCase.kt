package uz.khusinov.karvon.domain.use_case.profile

import kotlinx.coroutines.flow.Flow
import uz.khusinov.karvon.domain.model.card.AddCardRequest
import uz.khusinov.karvon.domain.model.card.AddCardResponse
import uz.khusinov.karvon.domain.repository.ProfileRepository
import uz.khusinov.karvon.utills.UiStateObject

class AddCardUseCase(
    private val repository: ProfileRepository
) {
    operator fun invoke(addCardRequest: AddCardRequest): Flow<UiStateObject<AddCardResponse>> {
        return repository.addCard(addCardRequest)
    }
}