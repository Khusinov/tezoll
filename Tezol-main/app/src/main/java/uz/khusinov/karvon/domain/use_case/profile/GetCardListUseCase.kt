package uz.khusinov.karvon.domain.use_case.profile

import kotlinx.coroutines.flow.Flow
import uz.khusinov.karvon.domain.model.card.CardResponse
import uz.khusinov.karvon.domain.repository.ProfileRepository
import uz.khusinov.karvon.utills.UiStateList

class GetCardListUseCase(
    private val repository: ProfileRepository
) {
    operator fun invoke(): Flow<UiStateList<CardResponse>> {
        return repository.getCards()
    }
}