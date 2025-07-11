package uz.khusinov.karvon.domain.use_case.auth

import kotlinx.coroutines.flow.Flow
import uz.khusinov.karvon.domain.model.Refresh
import uz.khusinov.karvon.domain.repository.AuthRepository
import uz.khusinov.karvon.utills.UiStateObject

class LogoutUseCase(
    private val repository: AuthRepository
) {
    operator fun invoke(refresh: Refresh): Flow<UiStateObject<Unit>> {
        return repository.logout(refresh = refresh)
    }
}