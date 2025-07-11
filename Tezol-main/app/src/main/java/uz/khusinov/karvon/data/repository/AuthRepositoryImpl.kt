package uz.khusinov.karvon.data.repository

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import uz.khusinov.karvon.data.remote.ApiService
import uz.khusinov.karvon.domain.model.ConfirmRequest
import uz.khusinov.karvon.domain.model.ConfirmResponse
import uz.khusinov.karvon.domain.model.LoginRequest
import uz.khusinov.karvon.domain.model.Refresh
import uz.khusinov.karvon.domain.repository.AuthRepository
import uz.khusinov.karvon.utills.userMessage
import uz.khusinov.karvon.utills.UiStateObject

class AuthRepositoryImpl(
    private val apiService: ApiService,
) : AuthRepository {

    override fun login(loginRequest: LoginRequest): Flow<UiStateObject<Unit>> = flow {
        emit(UiStateObject.LOADING)
        try {
            val response = apiService.login(loginRequest)
            if (response.success) {
                Log.d("TAG", "login: impl $response ")
                emit(UiStateObject.SUCCESS(response.data))
            } else
                emit(UiStateObject.ERROR(response.error.message))
        } catch (e: Exception) {
            emit(UiStateObject.ERROR(e.userMessage()))
        }
    }

    override fun logout(refresh: Refresh): Flow<UiStateObject<Unit>> = flow {
        emit(UiStateObject.LOADING)
        try {
            val response = apiService.logout(refresh)
            if (response.success) {
                emit(UiStateObject.SUCCESS(response.data))
            } else {
                emit(UiStateObject.ERROR(response.message))
            }
        } catch (e: Exception) {
            emit(UiStateObject.ERROR(e.userMessage()))
        }
    }

    override fun confirm(confirmRequest: ConfirmRequest): Flow<UiStateObject<ConfirmResponse>> =
        flow {
            emit(UiStateObject.LOADING)
            try {
                val response = apiService.confirm(confirmRequest)
                if (response.success) {
                    emit(UiStateObject.SUCCESS(response.data))
                } else {
                    emit(UiStateObject.ERROR(response.message))
                }
            } catch (e: Exception) {
                emit(UiStateObject.ERROR(e.userMessage()))
            }
        }
}