package uz.khusinov.karvon.domain.use_case.profile

import kotlinx.coroutines.flow.Flow
import uz.khusinov.karvon.domain.model.card.CardVerify
import uz.khusinov.karvon.domain.model.card.VerifyCardResponse
import uz.khusinov.karvon.domain.repository.ProfileRepository
import uz.khusinov.karvon.utills.UiStateObject

class VerifyCardUseCase(
    private val repository: ProfileRepository
) {
    operator fun invoke(cardVerify: CardVerify): Flow<UiStateObject<VerifyCardResponse>> {
        return repository.confirmCard(cardVerify)
    }
}