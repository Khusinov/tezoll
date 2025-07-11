package uz.khusinov.karvon.presentation.basket

import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import uz.khusinov.karvon.R
import uz.khusinov.karvon.databinding.FragmentConfirmBinding
import uz.khusinov.karvon.domain.model.CreateOrderRequest
import uz.khusinov.karvon.domain.model.OrderItem
import uz.khusinov.karvon.presentation.BaseFragment
import uz.khusinov.karvon.utills.launchAndRepeatWithViewLifecycle
import uz.khusinov.karvon.utills.price
import uz.khusinov.karvon.utills.UiStateList
import uz.khusinov.karvon.utills.UiStateObject
import uz.khusinov.karvon.utills.viewBinding

@AndroidEntryPoint
class ConfirmFragment : BaseFragment(R.layout.fragment_confirm) {

    private val binding by viewBinding { FragmentConfirmBinding.bind(it) }
    private val viewModel by activityViewModels<BasketViewModel>()
    private var deliveryPriceSelected = 0
    private var easyDeliveryPrice = 0
    private var fastDeliveryPrice = 0
    private var productsPrice = 0
    private val productsList = mutableListOf<OrderItem>()
    private var shopId = 0
    private var addressName = ""
    private var deliveryType = "standard"
    private var paymentType = "cash"
    private var fromLatitude = 0.0
    private var fromLongitude = 0.0


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupObserver()
        setupObserver2()
        setupObserver3()
        viewModel.getBasketProducts()

        val bundle = arguments
        if (bundle != null) {
            easyDeliveryPrice =
                bundle.getInt("standard", 0) // 0 is the default value if key is not found
            deliveryPriceSelected =
                bundle.getInt("standard", 0) // 0 is the default value if key is not found
            fastDeliveryPrice = bundle.getInt("express", 0)
            addressName = bundle.getString("addressName", "")
            fromLatitude = bundle.getDouble("fromLatitude", 0.0)
            fromLongitude = bundle.getDouble("fromLongitude", 0.0)
        }

        binding.apply {
            standardPriceTv.text = easyDeliveryPrice.toString()
            expressPriceTv.text = fastDeliveryPrice.toString()
        }
    }

    private fun setupObserver2() {
        launchAndRepeatWithViewLifecycle {
            viewModel.orderState.collect {
                when (it) {
                    is UiStateObject.LOADING -> {
                        showProgress()
                    }

                    is UiStateObject.SUCCESS -> {
                        hideProgress()
                        findNavController().navigate(R.id.action_confirmFragment_to_orderCreated)
                    }

                    is UiStateObject.ERROR -> {
                        hideProgress()
                    }

                    else -> {}
                }
            }
        }
    }

    private fun setupObserver() {
        launchAndRepeatWithViewLifecycle {
            viewModel.basketProductsState.collect {
                when (it) {
                    is UiStateList.LOADING -> {
                    }

                    is UiStateList.SUCCESS -> {
                        val selectedShop = viewModel.getSelectedShop().second
                        productsPrice =
                            it.data.filter { selectedProduct -> selectedProduct.shop == selectedShop }
                                .sumOf { it.price * it.selectedCount }
                        binding.productsPriceTv.text = price(productsPrice)
                        calculateOverallPrice(productsPrice, deliveryPriceSelected)
                    }

                    is UiStateList.ERROR -> {
                    }

                    else -> {}
                }
            }
        }
    }

    private fun setupUI() {
        binding.apply {
            val buttonTintColor = ContextCompat.getColor(requireContext(), R.color.black)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                delivery.buttonTintList = ColorStateList.valueOf(buttonTintColor) // Tugma rangi
                fastDelivery.buttonTintList = ColorStateList.valueOf(buttonTintColor) // Tugma rangi
                cash.buttonTintList = ColorStateList.valueOf(buttonTintColor) // Tugma rangi
                card.buttonTintList = ColorStateList.valueOf(buttonTintColor) // Tugma rangi
            } else {
                // API 21 dan past bo'lgan versiyalar uchun rang filtrini qo'llash
                delivery.buttonDrawable?.setColorFilter(
                    buttonTintColor,
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
                fastDelivery.buttonDrawable?.setColorFilter(
                    buttonTintColor,
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
                cash.buttonDrawable?.setColorFilter(
                    buttonTintColor,
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
                card.buttonDrawable?.setColorFilter(
                    buttonTintColor,
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
            }

            actionBack.setOnClickListener {
                findNavController().popBackStack()
            }

            selectAddress.setOnClickListener {
                // create order
                val createOrderRequest =
                    CreateOrderRequest(
                        address = addressName,
                        deliveryType = deliveryType,
                        paymentMethod = paymentType,
                        lat = fromLatitude,
                        lon = fromLongitude,
                        order_item = productsList,
                        shop = shopId
                    )

                viewModel.createOrder(createOrderRequest)
            }

            if (delivery.isChecked) {
                deliveryPrice.text = easyDeliveryPrice.toString()
                deliveryPriceSelected = easyDeliveryPrice
                deliveryType = "standard"
            }

            if (fastDelivery.isChecked) {
                deliveryPrice.text = fastDeliveryPrice.toString()
                deliveryPriceSelected = fastDeliveryPrice
                deliveryType = "express"
            }

            delivery.setOnCheckedChangeListener { buttonView, isChecked ->
                fastDelivery.isChecked = !isChecked
                deliveryPriceSelected = easyDeliveryPrice
                calculateOverallPrice(productsPrice, deliveryPriceSelected)
                deliveryType = "standard"
            }

            fastDelivery.setOnCheckedChangeListener { buttonView, isChecked ->
                delivery.isChecked = !isChecked
                deliveryPriceSelected = fastDeliveryPrice
                calculateOverallPrice(productsPrice, deliveryPriceSelected)
                deliveryType = "express"
            }

            cash.setOnCheckedChangeListener { buttonView, isChecked ->
                binding.card.isChecked = !isChecked
                paymentType = "cash"
            }

            card.setOnCheckedChangeListener { buttonView, isChecked ->
                binding.cash.isChecked = !isChecked
                paymentType = "card"
            }
        }
    }

    fun calculateOverallPrice(productsPrice: Int, deliveryPrice: Int) {
        binding.overallPrice.text = price(productsPrice + deliveryPrice)
        binding.deliveryPrice.text = price(deliveryPrice)
    }

    private fun setupObserver3() {
        launchAndRepeatWithViewLifecycle {
            viewModel.basketProductsState.collect {
                when (it) {
                    is UiStateList.LOADING -> {
                    }

                    is UiStateList.SUCCESS -> {
                        val selectedShopId = viewModel.getSelectedShop().second
                        if (it.data.isNotEmpty()) {
                            shopId = it.data.first().shop
                            productsList.clear()
                            for (i in it.data) {
                                if (i.shop == selectedShopId) {
                                    val productItem = OrderItem(0, 0)
                                    productItem.product_type = i.id
                                    productItem.quantity = i.selectedCount
                                    productsList.add(productItem)
                                }
                            }
                        }
                    }

                    is UiStateList.ERROR -> {
                    }

                    else -> {}
                }
            }
        }
    }


}