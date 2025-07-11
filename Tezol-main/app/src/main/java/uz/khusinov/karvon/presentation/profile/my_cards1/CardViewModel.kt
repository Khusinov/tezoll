package uz.khusinov.karvon.presentation.profile.my_cards1

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uz.khusinov.karvon.domain.model.card.AddCardRequest
import uz.khusinov.karvon.domain.model.card.AddCardResponse
import uz.khusinov.karvon.domain.model.card.CardResponse
import uz.khusinov.karvon.domain.model.card.CardVerify
import uz.khusinov.karvon.domain.model.card.VerifyCardResponse
import uz.khusinov.karvon.domain.use_case.profile.ProfileUseCases
import uz.khusinov.karvon.utills.UiStateList
import uz.khusinov.karvon.utills.UiStateObject
import javax.inject.Inject

/**
 * ViewModel for managing card-related operations in the profile section.
 * Handles adding, deleting, verifying, and setting main cards, as well as retrieving card lists.
 */
@HiltViewModel
class CardViewModel @Inject constructor(
    private val profileUseCases: ProfileUseCases
) : ViewModel() {

    // State flows for UI state management
    private val _addCardState = MutableSharedFlow<UiStateObject<AddCardResponse>>()
    val addCardState = _addCardState.asSharedFlow()

    private val _deleteCardState = MutableSharedFlow<UiStateObject<String>>()
    val deleteCardState = _deleteCardState.asSharedFlow()

    private val _mainCardState = MutableSharedFlow<UiStateObject<String>>()
    val mainCardState = _mainCardState.asSharedFlow()

    private val _getCardState = MutableStateFlow<UiStateList<CardResponse>>(UiStateList.EMPTY)
    val getCardState = _getCardState.asStateFlow()

    private val _confirmCardState = MutableSharedFlow<UiStateObject<VerifyCardResponse>>()
    val confirmCardState = _confirmCardState.asSharedFlow()

    /**
     * Generic function to handle state emission for single-object responses.
     */
    private inline fun <T, reified S> handleStateEmission(
        flow: MutableSharedFlow<UiStateObject<S>>,
        result: UiStateObject<T>
    ) {
        viewModelScope.launch {
            when (result) {
                is UiStateObject.LOADING -> flow.emit(UiStateObject.LOADING)
                is UiStateObject.SUCCESS -> flow.emit(UiStateObject.SUCCESS(result.data as S))
                is UiStateObject.ERROR -> flow.emit(UiStateObject.ERROR(result.message))
                is UiStateObject.EMPTY -> flow.emit(UiStateObject.EMPTY)
            }
        }
    }

    /**
     * Retrieves the list of user's cards.
     */
    fun getCards() {
        profileUseCases.getCardListUseCase().onEach { result ->
            _getCardState.emit(when (result) {
                is UiStateList.LOADING -> UiStateList.LOADING
                is UiStateList.SUCCESS -> UiStateList.SUCCESS(result.data)
                is UiStateList.ERROR -> UiStateList.ERROR(result.message)
                is UiStateList.EMPTY -> UiStateList.EMPTY
            })
        }.launchIn(viewModelScope)
    }

    /**
     * Adds a new card to the user's profile.
     * @param addCardRequest The request containing card details
     */
    fun addCard(addCardRequest: AddCardRequest) {
        profileUseCases.addCardUseCase(addCardRequest).onEach { result ->
            handleStateEmission(_addCardState, result)
        }.launchIn(viewModelScope)
    }

    /**
     * Verifies a card using the provided verification details.
     * @param confirm The card verification details
     */
    fun confirmCard(confirm: CardVerify) {
        profileUseCases.verifyCardUseCase(confirm).onEach { result ->
            handleStateEmission(_confirmCardState, result)
        }.launchIn(viewModelScope)
    }

    /**
     * Deletes a card by its ID.
     * @param cardId The ID of the card to delete
     */
    fun deleteCard(cardId: String) {
        profileUseCases.removeCardUseCase(cardId).onEach { result ->
            handleStateEmission(_deleteCardState, result)
        }.launchIn(viewModelScope)
    }

    /**
     * Sets a card as the main card.
     * @param cardId The ID of the card to set as main
     */
    fun setMainCard(cardId: String) {
        profileUseCases.setMainCardUseCase(cardId).onEach { result ->
            handleStateEmission(_mainCardState, result)
        }.launchIn(viewModelScope)
    }
}