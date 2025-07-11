package uz.khusinov.karvon.domain.repository

import kotlinx.coroutines.flow.Flow
import uz.khusinov.karvon.domain.model.ConfirmRequest
import uz.khusinov.karvon.domain.model.ConfirmResponse
import uz.khusinov.karvon.domain.model.LoginRequest
import uz.khusinov.karvon.domain.model.Refresh
import uz.khusinov.karvon.utills.UiStateObject

interface AuthRepository {
    fun login(loginRequest: LoginRequest): Flow<UiStateObject<Unit>>

    fun logout(refresh: Refresh): Flow<UiStateObject<Unit>>

    fun confirm(confirmRequest: ConfirmRequest): Flow<UiStateObject<ConfirmResponse>>
}