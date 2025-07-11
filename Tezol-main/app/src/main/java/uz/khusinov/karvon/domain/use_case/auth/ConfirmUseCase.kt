package uz.khusinov.karvon.domain.use_case.auth

import kotlinx.coroutines.flow.Flow
import uz.khusinov.karvon.domain.model.ConfirmRequest
import uz.khusinov.karvon.domain.model.ConfirmResponse
import uz.khusinov.karvon.domain.repository.AuthRepository
import uz.khusinov.karvon.utills.UiStateObject

class ConfirmUseCase(
    private val repository: AuthRepository
) {

    operator fun invoke(confirmRequest: ConfirmRequest): Flow<UiStateObject<ConfirmResponse>> {
        return repository.confirm(confirmRequest)
    }
}