package uz.khusinov.karvon.domain.repository

import kotlinx.coroutines.flow.Flow
import uz.khusinov.karvon.domain.model.User
import uz.khusinov.karvon.domain.model.card.AddCardRequest
import uz.khusinov.karvon.domain.model.card.AddCardResponse
import uz.khusinov.karvon.domain.model.card.CardResponse
import uz.khusinov.karvon.domain.model.card.CardVerify
import uz.khusinov.karvon.domain.model.card.VerifyCardResponse
import uz.khusinov.karvon.utills.UiStateList
import uz.khusinov.karvon.utills.UiStateObject

interface ProfileRepository {
    fun getMe(): Flow<UiStateObject<User>>
    fun getCards(): Flow<UiStateList<CardResponse>>
    fun addCard(addCardRequest: AddCardRequest): Flow<UiStateObject<AddCardResponse>>
    fun confirmCard(confirm: CardVerify): Flow<UiStateObject<VerifyCardResponse>>
    fun deleteCard(id: String): Flow<UiStateObject<String>>
    fun setMainCard(id: String): Flow<UiStateObject<String>>
}