package uz.khusinov.karvon.domain.use_case.profile

import kotlinx.coroutines.flow.Flow
import uz.khusinov.karvon.domain.repository.ProfileRepository
import uz.khusinov.karvon.utills.UiStateObject

class RemoveCardUseCase(
    private val repository: ProfileRepository
) {
    operator fun invoke(cardId: String): Flow<UiStateObject<String>> {
        return repository.deleteCard(cardId)
    }
}