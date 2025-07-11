package uz.khusinov.karvon.presentation.shops

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.khusinov.karvon.data.remote.ApiService
import uz.khusinov.karvon.domain.model.base.BasePagination
import uz.khusinov.karvon.domain.model.shop.Product
import uz.khusinov.karvon.domain.model.shop.SelectedProduct
import uz.khusinov.karvon.domain.model.shop.Shop
import uz.khusinov.karvon.domain.use_case.shops.ShopPagingSource
import uz.khusinov.karvon.domain.use_case.shops.ShopsUseCases
import uz.khusinov.karvon.utills.UiStateList
import uz.khusinov.karvon.utills.UiStateObject
import javax.inject.Inject

@HiltViewModel
class ShopsViewModel @Inject constructor(
    private val shopsUseCases: ShopsUseCases,
    private val apiService: ApiService,

    ) : ViewModel() {

    val shopsPaging: Flow<PagingData<Shop>> = Pager(
        config = PagingConfig(
            pageSize = 10,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { ShopPagingSource(apiService) }
    ).flow.cachedIn(viewModelScope)

    private val _productState = MutableSharedFlow<UiStateList<Product>>()
    val productState = _productState


    fun getProducts(shopId: Int) {
        shopsUseCases.getProductsUseCase.invoke(shopId).onEach { result ->

            when (result) {
                is UiStateObject.LOADING -> _productState.emit(UiStateList.LOADING)

                is UiStateObject.SUCCESS -> _productState.emit(UiStateList.SUCCESS(result.data.results))

                is UiStateObject.ERROR -> _productState.emit(UiStateList.ERROR(result.message))

                else -> {}
            }
        }.launchIn(viewModelScope)
    }

    private val _categoryProductState = MutableStateFlow<UiStateObject<BasePagination<Product>>>(
        UiStateObject.EMPTY)
    val categoryProductState = _categoryProductState


    fun getCategoryProducts(categoryId: Int) {
        shopsUseCases.getCategoryProductsUseCase.invoke(categoryId).onEach { result ->

            when (result) {
                is UiStateObject.LOADING -> _categoryProductState.emit(UiStateObject.LOADING)

                is UiStateObject.SUCCESS -> _categoryProductState.emit(UiStateObject.SUCCESS(result.data))

                is UiStateObject.ERROR -> _categoryProductState.emit(UiStateObject.ERROR(result.message))

                else -> {}
            }
        }.launchIn(viewModelScope)
    }

    private val _basket = MutableStateFlow<List<SelectedProduct>>(emptyList())
    val basket: StateFlow<List<SelectedProduct>> = _basket.asStateFlow()
    var shop: Shop? = null

    fun clearBasket(shop: Shop? = null) {
        _basket.value = emptyList()
        this.shop = shop
    }

}