package uz.khusinov.karvon.domain.use_case.profile

import kotlinx.coroutines.flow.Flow
import uz.khusinov.karvon.domain.model.User
import uz.khusinov.karvon.domain.repository.ProfileRepository
import uz.khusinov.karvon.utills.UiStateObject

class GetMeUseCase(
    private val repository: ProfileRepository
) {
    operator fun invoke(): Flow<UiStateObject<User>> {
        return repository.getMe()
    }
}