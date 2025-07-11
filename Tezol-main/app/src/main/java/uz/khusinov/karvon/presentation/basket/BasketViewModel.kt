package uz.khusinov.karvon.presentation.basket

import FullAddress
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uz.khusinov.karvon.SharedPref
import uz.khusinov.karvon.domain.model.CreateOrderRequest
import uz.khusinov.karvon.domain.model.CreateOrderResponse
import uz.khusinov.karvon.domain.model.DeliveryPrice
import uz.khusinov.karvon.domain.model.GetDeliveryPriceRequest
import uz.khusinov.karvon.domain.model.shop.SelectedProduct
import uz.khusinov.karvon.domain.use_case.products.ProductsUseCases
import uz.khusinov.karvon.domain.use_case.basket.BasketUseCases
import uz.khusinov.karvon.utills.UiStateList
import uz.khusinov.karvon.utills.UiStateObject
import javax.inject.Inject


@HiltViewModel
class BasketViewModel @Inject constructor(
    private val productUseCase: ProductsUseCases,
    private val basketUseCase: BasketUseCases,
    private val sharedPref:SharedPref
) : ViewModel() {

    private var selectedShopId :Int = 0
    private var selectedShopName:String = ""

    fun updateSelectedShop(shopId:Int, name:String){
        selectedShopName = name
        selectedShopId = shopId
        sharedPref.selectedShopId = shopId.toString()
    }

    fun getSelectedShop(): Pair<String, Int>{
        return Pair(first = selectedShopName, second = sharedPref.selectedShopId.toInt())
    }


    fun insertProductToBasket(product: SelectedProduct) {
        viewModelScope.launch(Dispatchers.IO) {
            productUseCase.insertProductUseCase.invoke(product)
        }
    }

    fun updateProduct(products: SelectedProduct) =
        viewModelScope.launch {
            productUseCase.updateProductUseCase.invoke(products)
        }

    fun deleteProduct(products: SelectedProduct) =
        viewModelScope.launch {
            productUseCase.removeProductUseCase.invoke(products)
        }

    fun deleteAllProducts() {
        viewModelScope.launch {
            productUseCase.deleteAllProductUseCase.invoke()
        }
    }

    private fun deleteProductsByShop(shopId:Int) {
        viewModelScope.launch {
            productUseCase.deleteProductByShopUseCase.invoke(shopId)
        }
    }


    private val _basketProductsState =
        MutableStateFlow<UiStateList<SelectedProduct>>(UiStateList.EMPTY)
    val basketProductsState = _basketProductsState

    fun getBasketProducts() {
        productUseCase.getProductsUseCase.invoke().onEach { result ->
            when (result) {
                is UiStateList.LOADING -> _basketProductsState.emit(UiStateList.LOADING)

                is UiStateList.SUCCESS -> {
                    _basketProductsState.emit(UiStateList.SUCCESS(result.data))
                    _basket.value = result.data
                }

                is UiStateList.ERROR -> _basketProductsState.emit(UiStateList.ERROR(result.message))

                else -> {
                }
            }
        }.launchIn(viewModelScope)
    }


    private val _basket = MutableStateFlow<List<SelectedProduct>>(emptyList())
    val basket: StateFlow<List<SelectedProduct>> = _basket.asStateFlow()

    var shopId: Int? = null


    fun clearBasket(shopId1: Int? = null) {
        _basket.value = emptyList()
        this.shopId = shopId1
        deleteAllProducts()
    }

    private val _addressNameFromOpenStreet =
        MutableStateFlow<UiStateObject<FullAddress>>(UiStateObject.EMPTY)
    val addressNameFromOpenStreet = _addressNameFromOpenStreet
    fun getAddressNameFromOpenStreet(lat: Double, long: Double) {
        basketUseCase.addressUseCase.invoke(lat, long).onEach { result ->
            when (result) {
                is UiStateObject.LOADING -> _addressNameFromOpenStreet.emit(UiStateObject.LOADING)

                is UiStateObject.SUCCESS -> _addressNameFromOpenStreet.emit(
                    UiStateObject.SUCCESS(
                        result.data
                    )
                )

                is UiStateObject.ERROR -> _addressNameFromOpenStreet.emit(UiStateObject.ERROR(result.message))

                else -> {

                }
            }
        }.launchIn(viewModelScope)
    }

    private val _orderState =
        MutableStateFlow<UiStateObject<Unit>>(UiStateObject.EMPTY)
    val orderState = _orderState
    fun createOrder(createOrderRequest: CreateOrderRequest) {
        basketUseCase.basketUseCase.invoke("", createOrderRequest).onEach { result ->
            when (result) {
                is UiStateObject.LOADING -> _orderState.emit(UiStateObject.LOADING)

                is UiStateObject.SUCCESS -> {
                    deleteProductsByShop(selectedShopId)
                    _orderState.emit(
                        UiStateObject.SUCCESS(
                            result.data
                        )
                    )
                }


                is UiStateObject.ERROR -> _orderState.emit(UiStateObject.ERROR(result.message))

                else -> {

                }
            }
        }.launchIn(viewModelScope)
    }


    private val _deliveryPrice =
        MutableStateFlow<UiStateObject<DeliveryPrice>>(UiStateObject.EMPTY)
    val deliveryPrice = _deliveryPrice

    fun getDeliveryPrice(deliveryPriceRequest: GetDeliveryPriceRequest) {
        basketUseCase.getDeliveryPriceUseCase.invoke(deliveryPriceRequest).onEach { result ->
            when (result) {
                is UiStateObject.LOADING -> _deliveryPrice.emit(UiStateObject.LOADING)

                is UiStateObject.SUCCESS -> {
                    println("Success")
                    Log.d("TAG", "getDeliveryPrice: success ")
                    _deliveryPrice.emit(
                        UiStateObject.SUCCESS(
                            result.data
                        )
                    )
                }

                is UiStateObject.ERROR -> _deliveryPrice.emit(UiStateObject.ERROR(result.message))

                else -> {

                }
            }
        }.launchIn(viewModelScope)
    }


}
