package uz.khusinov.karvon.presentation.catalog.selected_category

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import uz.khusinov.karvon.R
import uz.khusinov.karvon.databinding.FragmentSelectedCategoryBinding
import uz.khusinov.karvon.domain.model.shop.Product
import uz.khusinov.karvon.domain.model.shop.SelectedProduct
import uz.khusinov.karvon.presentation.BaseFragment
import uz.khusinov.karvon.presentation.basket.BasketViewModel
import uz.khusinov.karvon.presentation.home.components.HomeProductAdapter
import uz.khusinov.karvon.presentation.shops.ShopsViewModel
import uz.khusinov.karvon.presentation.shops.components.Action
import uz.khusinov.karvon.presentation.shops.components.ShopsConfirmDialog
import uz.khusinov.karvon.presentation.shops.selectedShop.ProductDetailsBottomSheet
import uz.khusinov.karvon.utills.launchAndRepeatWithViewLifecycle
import uz.khusinov.karvon.utills.UiStateObject
import uz.khusinov.karvon.utills.calculateDiscount
import uz.khusinov.karvon.utills.viewBinding

class SelectedCategoryFragment : BaseFragment(R.layout.fragment_selected_category) {
    private val binding by viewBinding { FragmentSelectedCategoryBinding.bind(it) }
    private val shopViewModel by viewModels<ShopsViewModel>()
    private val basketViewModel by activityViewModels<BasketViewModel>()

    private val newProductAdapter by lazy {
        HomeProductAdapter { product ->
            navigateToProductDetail(
                product
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shopViewModel.getCategoryProducts(arguments?.getInt("categoryId") ?: 0)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupCategoryProductsObserver()
        binding.toolbarTitle.text = arguments?.getString("title")
    }

    private fun setupUI() = with(binding) {
        toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        recyclerView.adapter = newProductAdapter
    }

    private fun setupCategoryProductsObserver() {
        launchAndRepeatWithViewLifecycle {
            shopViewModel.categoryProductState.collect {
                when (it) {
                    is UiStateObject.LOADING -> {
                        showProgress()
                    }

                    is UiStateObject.SUCCESS -> {
                        hideProgress()
                        newProductAdapter.submitList(it.data.results.filter { it.product_type.isNotEmpty() })
                    }

                    is UiStateObject.ERROR -> {
                        hideProgress()
                        showToast(it.message)
                    }

                    else -> {
                    }
                }
            }
        }
    }

    private var lastClickTime = 0L
    private fun navigateToProductDetail(product: Product) {
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
            image = if (product.image != null) product.image else "",
            is_active = product.is_active,
            is_liked = product.is_liked,
            name = product.name,
            percent = product.percent,
            shop = product.shop.id,
            shopName = product.shop.name,
            stars_count = product.stars_count,
            count = product.product_type[0].count,
            discount = product.product_type[0].discount?.discount?: "null",
            discountInPrice = product.product_type[0].discount?.discount_in_price ?: 0.0,
            discount_type = product.product_type[0].discount_type ?: "null",
            id = product.product_type[0].id,
            measurement_unit = product.product_type[0].measurement_unit,
            price = calculateDiscount(price = product.product_type.first().price, discount = product.product_type.first().discount, discountType = product.product_type.first().discount_type ),//product.product_type[0].price,
            product = product.product_type[0].product,
            quantity_type = product.product_type[0].quantity_type,
            selectedCount = count
        )
//        if (basketViewModel.shopId != product.shop.id && basketViewModel.basket.value.isNotEmpty()) {
//            val dialog = ShopsConfirmDialog(
//                title = getString(R.string.clear_basket),
//                message = getString(R.string.clear_basket_msg),
//                action = Action(getString(R.string.yes), true) {
//                    basketViewModel.clearBasket(product.shop.id)
//                    Toast.makeText(requireContext(), "Savatga qo'shildi", Toast.LENGTH_SHORT).show()
//                    basketViewModel.getBasketProducts()
//                    basketViewModel.insertProductToBasket(selectedProduct)
//                },
//                secondAction = Action(getString(R.string.no), false) {}
//            )
//            dialog.show(parentFragmentManager, null)
//        } else {
            basketViewModel.shopId = product.shop.id
            basketViewModel.insertProductToBasket(selectedProduct)
            Toast.makeText(requireContext(), "Savatga qo'shildi", Toast.LENGTH_SHORT).show()
            basketViewModel.getBasketProducts()
       // }
    }

}