package uz.khusinov.karvon.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.khusinov.karvon.domain.model.Data
import uz.khusinov.karvon.domain.model.OrderHistory
import uz.khusinov.karvon.domain.use_case.orders.OrdersUseCases
import uz.khusinov.karvon.utills.UiStateList
import uz.khusinov.karvon.utills.UiStateObject
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val ordersUseCase: OrdersUseCases
) : ViewModel() {

    private val _ordersHistoryState = MutableStateFlow<UiStateObject<Data<OrderHistory>>>(UiStateObject.EMPTY)
    val ordersHistoryState = _ordersHistoryState

    fun getOrdersHistory() {
        ordersUseCase.getOrdersHistoryUseCase.invoke().onEach { result ->
            when (result) {
                is UiStateObject.LOADING -> _ordersHistoryState.emit(UiStateObject.LOADING)

                is UiStateObject.SUCCESS -> _ordersHistoryState.emit(UiStateObject.SUCCESS(result.data))

                is UiStateObject.ERROR -> _ordersHistoryState.emit(UiStateObject.ERROR(result.message))

                else -> {}
            }
        }.launchIn(viewModelScope)
    }
}