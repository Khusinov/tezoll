package uz.khusinov.karvon.data.repository

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import uz.khusinov.karvon.data.remote.ApiService
import uz.khusinov.karvon.domain.model.User
import uz.khusinov.karvon.domain.model.card.AddCardRequest
import uz.khusinov.karvon.domain.model.card.AddCardResponse
import uz.khusinov.karvon.domain.model.card.CardResponse
import uz.khusinov.karvon.domain.model.card.CardVerify
import uz.khusinov.karvon.domain.model.card.VerifyCardResponse
import uz.khusinov.karvon.domain.repository.ProfileRepository
import uz.khusinov.karvon.utills.UiStateList
import uz.khusinov.karvon.utills.userMessage
import uz.khusinov.karvon.utills.UiStateObject

class ProfileRepositoryImpl(
    private val apiService: ApiService,
) : ProfileRepository {

    override fun getMe(): Flow<UiStateObject<User>> = flow {
        emit(UiStateObject.LOADING)
        try {
            val response = apiService.getMe()
            if (response.success) {
                emit(UiStateObject.SUCCESS(response.data))
            } else
                emit(UiStateObject.ERROR(response.error.message))
        } catch (e: Exception) {
            Log.d("TAG", "getMe: ${e.message}")
            emit(UiStateObject.ERROR(e.userMessage()))
        }
    }

    override fun getCards(): Flow<UiStateList<CardResponse>> = flow {
        emit(UiStateList.LOADING)
        try {
            val response = apiService.getCards()
            if (response.success) {
                emit(UiStateList.SUCCESS(response.data))
            } else {
                emit(UiStateList.ERROR("Error getting cards"))
            }
        } catch (e: Exception) {
            emit(UiStateList.ERROR(e.userMessage()))
        }
    }

    override fun addCard(addCardRequest: AddCardRequest): Flow<UiStateObject<AddCardResponse>> = flow {
        emit(UiStateObject.LOADING)
        try {
            val response = apiService.addCard(addCardRequest)
            if (response.success) {
                emit(UiStateObject.SUCCESS(response.data))
            } else {
                emit(UiStateObject.ERROR(response.error.message))
            }
        } catch (e: Exception) {
            emit(UiStateObject.ERROR(e.userMessage()))
        }

    }

    override fun confirmCard(confirm: CardVerify): Flow<UiStateObject<VerifyCardResponse>> = flow {
        emit(UiStateObject.LOADING)
        try {
            val response = apiService.confirmCard(confirm)
            if (response.success) {
                emit(UiStateObject.SUCCESS(response.data))
            } else {
                emit(UiStateObject.ERROR(response.error.message))
            }
        } catch (e: Exception) {
            emit(UiStateObject.ERROR(e.userMessage()))
        }
    }

    override fun deleteCard(id: String): Flow<UiStateObject<String>> = flow {
        emit(UiStateObject.LOADING)
        try {
            val response = apiService.deleteCard(id)
            if (response.success) {
                emit(UiStateObject.SUCCESS(response.data))
            } else {
                emit(UiStateObject.ERROR(response.error.message))
            }
        } catch (e: Exception) {
            emit(UiStateObject.ERROR(e.userMessage()))
        }
    }

    override fun setMainCard(id: String): Flow<UiStateObject<String>> = flow {
        emit(UiStateObject.LOADING)
        try {
            val response = apiService.setMainCard(id)
            if (response.success) {
                emit(UiStateObject.SUCCESS(response.data))
            } else {
                emit(UiStateObject.ERROR(response.error.message))
            }
        } catch (e: Exception) {
            emit(UiStateObject.ERROR(e.userMessage()))
        }
    }


}