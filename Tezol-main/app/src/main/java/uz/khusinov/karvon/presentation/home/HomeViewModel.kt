package uz.khusinov.karvon.presentation.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.khusinov.karvon.data.remote.ApiService
import uz.khusinov.karvon.domain.model.AdsResponse
import uz.khusinov.karvon.domain.model.Data
import uz.khusinov.karvon.domain.model.shop.Product
import uz.khusinov.karvon.domain.use_case.home.HomeUseCases
import uz.khusinov.karvon.domain.use_case.home.pagingSourses.NewProductPagingSource
import uz.khusinov.karvon.utills.UiStateObject
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeUseCases: HomeUseCases,
    private val apiService: ApiService,
    ) : ViewModel() {

    private val _getAdsState = MutableStateFlow<UiStateObject<AdsResponse>>(UiStateObject.EMPTY)
    val getAdsState = _getAdsState

    fun getAds() {
        homeUseCases.getAdsUseCase.invoke().onEach { result ->
            when (result) {
                is UiStateObject.LOADING -> _getAdsState.emit(UiStateObject.LOADING)

                is UiStateObject.SUCCESS -> _getAdsState.emit(UiStateObject.SUCCESS(result.data))

                is UiStateObject.ERROR -> _getAdsState.emit(UiStateObject.ERROR(result.message))

                else -> {}
            }
        }.launchIn(viewModelScope)
    }

    private val _searchProductState =
        MutableStateFlow<UiStateObject<Data<Product>>>(UiStateObject.EMPTY)
    val searchProductState = _searchProductState

    fun searchProducts(query: String) {
        homeUseCases.searchProductsUseCase.invoke(query).onEach { result ->
            when (result) {
                is UiStateObject.LOADING -> _searchProductState.emit(UiStateObject.LOADING)

                is UiStateObject.SUCCESS -> _searchProductState.emit(UiStateObject.SUCCESS(result.data))

                is UiStateObject.ERROR -> _searchProductState.emit(UiStateObject.ERROR(result.message))

                else -> {}
            }
        }.launchIn(viewModelScope)
    }


    // code yozdim @TODO
    val newProductPaging: Flow<PagingData<Product>> = Pager(
        config = PagingConfig(
            pageSize = 10,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { NewProductPagingSource(apiService) }
    ).flow.cachedIn(viewModelScope)

    private val _getTopProductsState =
        MutableStateFlow<UiStateObject<Data<Product>>>(UiStateObject.EMPTY)
    val getTopProductsState = _getTopProductsState

    fun getTopProducts() {
        homeUseCases.getTopProductsUseCase.invoke().onEach { result ->
            when (result) {
                is UiStateObject.LOADING -> _getTopProductsState.emit(UiStateObject.LOADING)

                is UiStateObject.SUCCESS -> _getTopProductsState.emit(UiStateObject.SUCCESS(result.data))

                is UiStateObject.ERROR -> _getTopProductsState.emit(UiStateObject.ERROR(result.message))

                else -> {}
            }
        }.launchIn(viewModelScope)
    }

    private val _getMostSellProductsState =
        MutableStateFlow<UiStateObject<Data<Product>>>(UiStateObject.EMPTY)
    val getMostSellProductsState = _getMostSellProductsState

    fun getMostSellProducts() {
        homeUseCases.getMostSellProductsUseCase.invoke().onEach { result ->
            when (result) {
                is UiStateObject.LOADING -> _getMostSellProductsState.emit(UiStateObject.LOADING)

                is UiStateObject.SUCCESS -> _getMostSellProductsState.emit(
                    UiStateObject.SUCCESS(
                        result.data
                    )
                )

                is UiStateObject.ERROR -> _getMostSellProductsState.emit(UiStateObject.ERROR(result.message))

                else -> {}
            }
        }.launchIn(viewModelScope)
    }

    val isInsideCity: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
}