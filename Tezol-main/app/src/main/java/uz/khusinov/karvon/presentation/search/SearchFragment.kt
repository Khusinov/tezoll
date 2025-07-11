package uz.khusinov.karvon.presentation.search

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.khusinov.karvon.R
import uz.khusinov.karvon.databinding.FragmentSearchBinding
import uz.khusinov.karvon.domain.model.shop.Product
import uz.khusinov.karvon.domain.model.shop.SelectedProduct
import uz.khusinov.karvon.domain.model.shop.Shop
import uz.khusinov.karvon.presentation.BaseFragment
import uz.khusinov.karvon.presentation.basket.BasketViewModel
import uz.khusinov.karvon.presentation.home.HomeViewModel
import uz.khusinov.karvon.presentation.search.adapter.SearchAdapter
import uz.khusinov.karvon.presentation.shops.components.Action
import uz.khusinov.karvon.presentation.shops.components.ShopsConfirmDialog
import uz.khusinov.karvon.presentation.shops.selectedShop.ProductDetailsBottomSheet
import uz.khusinov.karvon.utills.launchAndRepeatWithViewLifecycle
import uz.khusinov.karvon.utills.UiStateObject
import uz.khusinov.karvon.utills.calculateDiscount
import uz.khusinov.karvon.utills.viewBinding

@AndroidEntryPoint
class SearchFragment : BaseFragment(R.layout.fragment_search) {

    private val binding by viewBinding { FragmentSearchBinding.bind(it) }
    private val viewModel by viewModels<HomeViewModel>()
    var restaurants: Shop? = null
    var shopId: Int? = null
    private var searchAddressJob: Job? = null
    private val basketViewModel by activityViewModels<BasketViewModel>()


    private val adapter by lazy {
        SearchAdapter(
            this::onItemClicked,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupObserver()
    }

    private fun setupUI() = with(binding) {

        rv.adapter = adapter

        search.doAfterTextChanged {
            if (search.text.toString().isEmpty() || search.text.toString().length < 2) {
                adapter.submitList(emptyList())
                return@doAfterTextChanged
            }

            searchAddressJob?.cancel()
            searchAddressJob = lifecycleScope.launch {
                delay(600)
                viewModel.searchProducts(
                    search.text.toString(),
                )
            }
        }
    }

    private var lastClickTime = 0L


    private fun onItemClicked(product: Product) {
        if (System.currentTimeMillis() - lastClickTime < 1000) return
        lastClickTime = System.currentTimeMillis()
        val dialog = ProductDetailsBottomSheet()
        val bundle = Bundle()
        bundle.putParcelable("product", product)
        bundle.putInt("restaurantId", product.shop.id)
        dialog.arguments = bundle
        dialog.onAddToBasket = { count ->
            addToBasket(product, count)
        }
        dialog.show(parentFragmentManager, null)
    }

    private fun addToBasket(product: Product, count: Int = 1) {
        val selectedProduct = SelectedProduct(
            category = product.category.id,
            description = product.description,
            image = product.image,
            is_active = product.is_active,
            is_liked = product.is_liked,
            name = product.name,
            percent = product.percent,
            shop = product.shop.id,
            shopName = product.shop.name,
            stars_count = product.stars_count,
            count = product.product_type[0].count,
            discount = product.product_type[0].discount?.discount,
            discountInPrice = product.product_type[0].discount?.discount_in_price ?: 0.0,
            discount_type = product.product_type[0].discount_type!!,
            id = product.product_type[0].id,
            measurement_unit = product.product_type[0].measurement_unit,
            price = calculateDiscount(price = product.product_type.first().price, discount = product.product_type.first().discount, discountType = product.product_type.first().discount_type ),//product.product_type[0].price,
            product = product.product_type[0].product,
            quantity_type = product.product_type[0].quantity_type,
            selectedCount = count
        )

        Log.d("TAG", "addToBasket: ${basketViewModel.shopId} ")
        if (basketViewModel.shopId != product.shop.id && basketViewModel.basket.value.isNotEmpty()) {
            val dialog = ShopsConfirmDialog(
                title = getString(R.string.clear_basket),
                message = getString(R.string.clear_basket_msg),
                action = Action(getString(R.string.yes), true) {
                    basketViewModel.clearBasket(product.shop.id)
                    Toast.makeText(requireContext(), "Savatga qo'shildi", Toast.LENGTH_SHORT).show()
                    basketViewModel.getBasketProducts()
                    basketViewModel.insertProductToBasket(selectedProduct)
                },
                secondAction = Action(getString(R.string.no), false) {}
            )
            dialog.show(parentFragmentManager, null)
        } else {
            basketViewModel.shopId = product.shop.id
            basketViewModel.insertProductToBasket(selectedProduct)
            Toast.makeText(requireContext(), "Savatga qo'shildi", Toast.LENGTH_SHORT).show()
            basketViewModel.getBasketProducts()
        }
    }


    private fun setupObserver() {
        launchAndRepeatWithViewLifecycle {
            viewModel.searchProductState.collectLatest {
                when (it) {
                    is UiStateObject.LOADING -> {
                        showProgress()
                    }

                    is UiStateObject.SUCCESS -> {
                        hideProgress()
                        Log.d("TAG", "setupObserver: success ${it.data}")
                        if (it.data.results.isNotEmpty()) {
                            binding.productNotFoundTv.isVisible = false
                            binding.rv.isVisible = true
                            adapter.submitList(it.data.results.filter { it.product_type.isNotEmpty() })
                        } else {
                            binding.productNotFoundTv.isVisible = true
                            binding.rv.isVisible = false
                        }
                    }

                    is UiStateObject.ERROR -> {
                        hideProgress()
                        Log.d("TAG", "setupObserver: error ${it.message} ")
                    }

                    else -> {
                        Log.d("TAG", "setupObserver: error 2 ${it.toString()} ")
                    }
                }
            }
        }
    }

}