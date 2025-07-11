package uz.khusinov.karvon.presentation.home

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import uz.khusinov.karvon.R
import uz.khusinov.karvon.databinding.FragmentHomeBinding
import uz.khusinov.karvon.domain.model.shop.Product
import uz.khusinov.karvon.domain.model.shop.SelectedProduct
import uz.khusinov.karvon.domain.model.shop.Shop
import uz.khusinov.karvon.domain.model.shop.ShopNew
import uz.khusinov.karvon.presentation.BaseFragment
import uz.khusinov.karvon.presentation.basket.BasketViewModel
import uz.khusinov.karvon.presentation.home.components.AdsAdapter
import uz.khusinov.karvon.presentation.home.components.HomeProductAdapter
import uz.khusinov.karvon.presentation.home.components.HomeProductsAdapter
import uz.khusinov.karvon.presentation.shops.components.Action
import uz.khusinov.karvon.presentation.shops.components.ShopsConfirmDialog
import uz.khusinov.karvon.presentation.shops.selectedShop.ProductDetailsBottomSheet
import uz.khusinov.karvon.utills.launchAndRepeatWithViewLifecycle
import uz.khusinov.karvon.utills.navigateSafe
import uz.khusinov.karvon.utills.UiStateObject
import uz.khusinov.karvon.utills.calculateDiscount
import uz.khusinov.karvon.utills.viewBinding

@AndroidEntryPoint
class HomeFragment : BaseFragment(R.layout.fragment_home) {
    private val binding by viewBinding { FragmentHomeBinding.bind(it) }
    private val homeViewModel by viewModels<HomeViewModel>()
    private val basketViewModel by viewModels<BasketViewModel>()
    var shop: ShopNew? = null
    private val newProductAdapter by lazy {
        HomeProductsAdapter { product ->
            navigateToProductDetail(
                product
            )
        }
    }
    private val topProductAdapter by lazy {
        HomeProductAdapter { product ->
            navigateToProductDetail(
                product
            )
        }
    }
    private val mostSellProductAdapter by lazy {
        HomeProductAdapter { product ->
            navigateToProductDetail(
                product
            )
        }
    }

    private lateinit var adsAdapter: AdsAdapter
    private var pagerList: ArrayList<String> = ArrayList()
    var adsCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel.getAds()

        homeViewModel.getTopProducts()
        homeViewModel.getMostSellProducts()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupAdsObserver()
        setupNewProductObserver()
        setupTopProductObserver()
        setupMostSellProductObserver()
    }

    private fun setupUI() = with(binding) {
        rvNewProducts.adapter = newProductAdapter
        topProductsRv.adapter = topProductAdapter
        mostSoldRv.adapter = mostSellProductAdapter

        search.setOnClickListener {
            requireActivity().findNavController(R.id.main_container).navigateSafe(
                R.id.action_mainFragment_to_searchFragment
            )
        }
    }

    private fun setupAdsObserver() {
        launchAndRepeatWithViewLifecycle {
            homeViewModel.getAdsState.collect {
                when (it) {
                    is UiStateObject.LOADING -> showProgress()

                    is UiStateObject.SUCCESS -> {
                        hideProgress()
                        pagerList.clear()
                        pagerList.addAll(it.data.results)
                        setAds()
                    }

                    is UiStateObject.ERROR -> {
                        hideProgress()
                        showToast(it.message)
                    }

                    else -> Unit
                }
            }
        }
    }

    private fun setupNewProductObserver() {
        launchAndRepeatWithViewLifecycle {
            homeViewModel.newProductPaging.collectLatest { data ->
                Log.d("TAG", "setupNewProductObserver: $data ")
                newProductAdapter.submitData(data)
            }
        }
    }

    private fun setupTopProductObserver() {
        launchAndRepeatWithViewLifecycle {
            homeViewModel.getTopProductsState.collect {
                when (it) {
                    is UiStateObject.LOADING -> Unit

                    is UiStateObject.SUCCESS -> {
                        Log.d("TAG", "setupTopProductObserver: ${it.data} ")
                        binding.topLinear.isVisible =
                            it.data.results.filter { it.product_type.isNotEmpty() }.isNotEmpty()
                        binding.topProductsRv.isVisible =
                            it.data.results.filter { it.product_type.isNotEmpty() }.isNotEmpty()
                        topProductAdapter.submitList(it.data.results.filter { it.product_type.isNotEmpty() })
                    }

                    is UiStateObject.ERROR -> {
                        binding.topLinear.isVisible = false
                        binding.topProductsRv.isVisible = false
                        showToast(it.message)
                    }

                    else -> Unit
                }
            }
        }
    }

    private fun setupMostSellProductObserver() {
        launchAndRepeatWithViewLifecycle {
            homeViewModel.getMostSellProductsState.collect {
                when (it) {
                    is UiStateObject.LOADING -> Unit

                    is UiStateObject.SUCCESS -> {
                        Log.d("TAG", "setupMostSellProductObserver: ${it.data} ")
                        binding.mostSoldLinear.isVisible =
                            it.data.results.filter { it.product_type.isNotEmpty() }.isNotEmpty()
                        binding.mostSoldRv.isVisible =
                            it.data.results.filter { it.product_type.isNotEmpty() }.isNotEmpty()
                        mostSellProductAdapter.submitList(it.data.results.filter {
                            it.product_type
                                .isNotEmpty()
                        })
                    }

                    is UiStateObject.ERROR -> {
                        binding.mostSoldLinear.isVisible = false
                        binding.mostSoldRv.isVisible = false
                        showToast(it.message)
                    }

                    else -> Unit
                }
            }
        }
    }

    private fun setAds() = with(binding) {
        adsAdapter = AdsAdapter(requireContext(), pagerList)
        viewPager.adapter = adsAdapter
        viewPager.offscreenPageLimit = pagerList.size
        dotsIndicator.attachTo(viewPager)
        changeFlatImage()
    }

    private fun changeFlatImage() {
        adsCount++
        if (adsCount >= pagerList.size)
            adsCount = 0
        object : CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                    binding.viewPager.currentItem = adsCount
                    changeFlatImage()
                }
            }
        }.start()
    }

    private var lastClickTime = 0L
    private fun navigateToProductDetail(product: Product) {
        if (System.currentTimeMillis() - lastClickTime < 1000) return
        lastClickTime = System.currentTimeMillis()
        val dialog = ProductDetailsBottomSheet()
        val bundle = Bundle()
        shop = product.shop
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
            discount = product.product_type[0].discount?.discount ?: "null",
            discountInPrice = product.product_type[0].discount?.discount_in_price ?: 0.0,
            discount_type = product.product_type[0].discount_type ?: "null",
            id = product.product_type[0].id,
            measurement_unit = product.product_type[0].measurement_unit,
            price = calculateDiscount(price = product.product_type.first().price, discount = product.product_type.first().discount, discountType = product.product_type.first().discount_type ),//product.product_type[0].price,
            product = product.product_type[0].product,
            quantity_type = product.product_type[0].quantity_type,
            selectedCount = count
        )

//        if (basketViewModel.shopId != shop?.id && basketViewModel.basket.value.isNotEmpty()) {
//            val dialog = ShopsConfirmDialog(
//                title = getString(R.string.clear_basket),
//                message = getString(R.string.clear_basket_msg),
//                action = Action(getString(R.string.yes), true) {
//                    basketViewModel.clearBasket(shop?.id)
//                    Toast.makeText(requireContext(), "Savatga qo'shildi", Toast.LENGTH_SHORT).show()
//                    basketViewModel.getBasketProducts()
//                    basketViewModel.insertProductToBasket(selectedProduct)
//                },
//                secondAction = Action(getString(R.string.no), false) {}
//            )
//            dialog.show(parentFragmentManager, null)
//        } else {
            basketViewModel.shopId = shop!!.id
            basketViewModel.insertProductToBasket(selectedProduct)
            Toast.makeText(requireContext(), "Savatga qo'shildi", Toast.LENGTH_SHORT).show()
            basketViewModel.getBasketProducts()
//        }
    }
}