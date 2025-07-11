package uz.khusinov.karvon.presentation.basket

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import uz.khusinov.karvon.R
import uz.khusinov.karvon.databinding.FragmentBasketBinding
import uz.khusinov.karvon.domain.model.shop.BasketShop
import uz.khusinov.karvon.domain.model.shop.SelectedProduct
import uz.khusinov.karvon.presentation.BaseFragment
import uz.khusinov.karvon.presentation.basket.components.BasketAdapter
import uz.khusinov.karvon.presentation.basket.components.BasketShopsAdapter
import uz.khusinov.karvon.utills.launchAndRepeatWithViewLifecycle
import uz.khusinov.karvon.utills.price
import uz.khusinov.karvon.utills.UiStateList
import uz.khusinov.karvon.utills.viewBinding

@AndroidEntryPoint
class BasketFragment : BaseFragment(R.layout.fragment_basket) {
    private val binding by viewBinding { FragmentBasketBinding.bind(it) }
    private val viewModel by activityViewModels<BasketViewModel>()

    private val adapter by lazy {
        BasketAdapter(
            this::onDeleteItemClicked, this::onPlusClicked, this::onMinusClicked
        )
    }

    private val shopAdapter by lazy {
        BasketShopsAdapter(this::onClicked)
    }

    private val selectedShopProductsList = mutableListOf<SelectedProduct>()
    private var selectedShopId: Int? = null // Track selected shop ID
    private var selectedShopName: String = "" // Track selected shop ID
    private var basketShops = listOf<BasketShop>() // Store all shops

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupObserver()
        viewModel.getBasketProducts()
    }

    private fun setupObserver() {
        launchAndRepeatWithViewLifecycle {
            viewModel.basketProductsState.collect {
                when (it) {
                    is UiStateList.LOADING -> {
                        // Handle loading state if needed
                    }

                    is UiStateList.SUCCESS -> {
                        binding.llNoOrder.isVisible = it.data.isEmpty()
                        binding.llHasOrder.isVisible = it.data.isNotEmpty()

                        // Group products by shop
                        val groupedByShop = it.data.groupBy { it.shop }

                        // Create BasketShop list
                        basketShops = groupedByShop.map { (shopId, products) ->
                            val totalPrice = products.sumOf { product ->
                                (product.price * product.selectedCount).toDouble()
                            }
                            BasketShop(
                                id = shopId,
                                name = products.first().shopName,
                                price = totalPrice.toInt()
                            )
                        }

                        // Set default selected shop (first shop if available)
                        if (basketShops.isNotEmpty() && selectedShopId == null) {
                            selectedShopId = basketShops.first().id
                        }

                        // Update shops RecyclerView
                        binding.shopsRv.isVisible = basketShops.size > 1
                        shopAdapter.submitList(basketShops, selectedShopId)

                        // Filter products for selected shop
                        val selectedProducts = it.data.filter { it.shop == selectedShopId }
                        adapter.submitList(selectedProducts)

                        // Calculate total price for selected shop
                        val overallPrice = selectedProducts.sumOf { it.price * it.selectedCount }
                        binding.overallPrice.text = price(overallPrice)
                    }

                    is UiStateList.ERROR -> {
                        // Handle error state if needed
                    }

                    else -> {}
                }
            }
        }
    }

    private fun setupUI() {
        binding.apply {
            rvBasket.adapter = adapter
            shopsRv.adapter = shopAdapter

            basketDelete.setOnClickListener {
                viewModel.deleteAllProducts()
                Handler(Looper.getMainLooper()).postDelayed({
                    viewModel.getBasketProducts()
                }, 200)
            }

            btnConfirmation.setOnClickListener {
                if (selectedShopId != null && selectedShopId != 0)
                    viewModel.updateSelectedShop(selectedShopId!!, selectedShopName)

                requireActivity().findNavController(R.id.main_container)
                    .navigate(R.id.action_mainFragment_to_mapFragment)
            }
        }
    }

    private fun onDeleteItemClicked(position: Int) {
//        try {
//            viewModel.deleteProduct(adapter.currentList[position])
//        } catch (_: Exception) {
//        }
        Handler(Looper.getMainLooper()).postDelayed({
            viewModel.getBasketProducts()
        }, 200)
    }

    private fun onPlusClicked(products: SelectedProduct) {
        if (products.count >= products.selectedCount) {
            viewModel.updateProduct(products)
            Handler(Looper.getMainLooper()).postDelayed({
                viewModel.getBasketProducts()
            }, 200)
        } else {
            Toast.makeText(
                requireContext(),
                "Xozirda sotuvda ${products.count} ta maxsulot mavjud!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun onMinusClicked(products: SelectedProduct) {
        if (products.selectedCount > 0) {
            viewModel.updateProduct(products)
        } else {
            viewModel.deleteProduct(products)
        }
        Handler(Looper.getMainLooper()).postDelayed({
            viewModel.getBasketProducts()
        }, 200)
    }

    private fun onClicked(basketShop: BasketShop, isSelected: Boolean) {
        selectedShopId = basketShop.id
        viewModel.getBasketProducts() // Trigger re-collection to update UI
    }
}