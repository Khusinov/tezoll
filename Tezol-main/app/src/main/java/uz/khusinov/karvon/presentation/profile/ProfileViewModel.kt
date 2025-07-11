package uz.khusinov.karvon.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.khusinov.karvon.domain.model.User
import uz.khusinov.karvon.domain.use_case.profile.ProfileUseCases
import uz.khusinov.karvon.utills.UiStateObject
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileUseCases: ProfileUseCases
) : ViewModel() {

    private val _getMeState = MutableStateFlow<UiStateObject<User>>(UiStateObject.EMPTY)
    val getMeState = _getMeState

    fun getMe() {
        profileUseCases.getMeUseCase.invoke().onEach { result ->
            when (result) {
                is UiStateObject.LOADING -> _getMeState.emit(UiStateObject.LOADING)

                is UiStateObject.SUCCESS -> _getMeState.emit(UiStateObject.SUCCESS(result.data))

                is UiStateObject.ERROR -> _getMeState.emit(UiStateObject.ERROR(result.message))

                else -> {}
            }
        }.launchIn(viewModelScope)
    }




}