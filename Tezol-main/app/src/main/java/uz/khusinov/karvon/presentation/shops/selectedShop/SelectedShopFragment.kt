package uz.khusinov.karvon.presentation.shops.selectedShop

import android.os.Bundle
import android.view.View
import uz.khusinov.karvon.R
import uz.khusinov.karvon.databinding.FragmentSelectedShopBinding
import uz.khusinov.karvon.presentation.BaseFragment
import uz.khusinov.karvon.utills.viewBinding
import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.widget.Toast
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import uz.khusinov.karvon.SharedPref
import uz.khusinov.karvon.domain.model.shop.Product
import uz.khusinov.karvon.domain.model.shop.SelectedProduct
import uz.khusinov.karvon.domain.model.shop.Shop
import uz.khusinov.karvon.presentation.basket.BasketViewModel
import uz.khusinov.karvon.presentation.shops.ShopsViewModel
import uz.khusinov.karvon.presentation.shops.components.Action
import uz.khusinov.karvon.presentation.shops.components.ShopsConfirmDialog
import uz.khusinov.karvon.utills.launchAndRepeatWithViewLifecycle
import uz.khusinov.karvon.utills.UiStateList
import uz.khusinov.karvon.utills.calculateDiscount
import javax.inject.Inject

@AndroidEntryPoint
class SelectedShopFragment : BaseFragment(R.layout.fragment_selected_shop) {

    private val binding by viewBinding { FragmentSelectedShopBinding.bind(it) }
    var restaurants: Shop? = null
    var shopId: Int? = null
    private val viewModel by activityViewModels<ShopsViewModel>()
    private val basketViewModel by activityViewModels<BasketViewModel>()
    private val adapter by lazy {
        SelectedProductsAdapter(this::onItemClick)
    }

    @Inject
    lateinit var sharedPref: SharedPref

    private var motionProgress = 0f
    private lateinit var smoothScroller: RecyclerView.SmoothScroller

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        restaurants = arguments?.getParcelable("shop")
        shopId = arguments?.getInt("id")
        Log.d("TAG", "onCreate: rr $shopId ")
        Log.d("TAG", "onCreate: tt $restaurants ")
        if (restaurants != null)
            viewModel.getProducts(restaurants!!.id)
        else if (shopId != null) viewModel.getProducts(shopId!!)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupProductObserver()
        basketViewModel.getBasketProducts()
    }


    @SuppressLint("SetTextI18n")
    private fun setupUI() {
        binding.apply {

            motionLayout.addTransitionListener(object : MotionLayout.TransitionListener {
                override fun onTransitionStarted(
                    motionLayout: MotionLayout?,
                    startId: Int,
                    endId: Int
                ) {
                    ///  tvName.setTextColor(resources.getColor(R.color.white))
                }

                override fun onTransitionChange(
                    motionLayout: MotionLayout?,
                    startId: Int,
                    endId: Int,
                    progress: Float
                ) {
                    val color =
                        blendColors(resources.getColor(R.color.black), Color.WHITE, progress)
                    tvName.setTextColor(color)
                }

                override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                    if (currentId == R.id.start)
                        tvName.setTextColor(Color.WHITE)
                    else tvName.setTextColor(resources.getColor(R.color.black))
                }

                override fun onTransitionTrigger(
                    motionLayout: MotionLayout?,
                    triggerId: Int,
                    positive: Boolean,
                    progress: Float
                ) {

                }

            })

            recyclerView.adapter = adapter
            recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
            //  categoriesRow.adapter = categoryAdapter
            (recyclerView.itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false

            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val firstItemPosition = layoutManager.findFirstVisibleItemPosition()
                    val lastItemPosition = layoutManager.findLastVisibleItemPosition()
                    val firstFullItemPosition =
                        layoutManager.findFirstCompletelyVisibleItemPosition()
                    if (firstItemPosition != -1 && lastItemPosition != -1) {
                        val position =
                            if (firstFullItemPosition != -1) firstFullItemPosition else (firstItemPosition + lastItemPosition) / 2
                        // categoryAdapter.onSelectedItemChanged(position)
                        categoriesRow.scrollToPosition(position)
                    }
                }
            })

            backBtn.setOnClickListener {
                findNavController().navigateUp()
            }

//            infoBtn.setOnClickListener {
//                findNavController().navigate(
//                    R.id.action_selectedRestaurantFragment_to_restaurantInfoFragment,
//                    bundleOf("restaurant" to restaurants)
//                )
//            }

            basketBtn.setOnClickListener {
                sharedPref.toBasket = true
                findNavController().navigate(R.id.action_selectedShopFragment_to_mainFragment)
            }

            if (restaurants != null) {
                if (restaurants!!.image != null)
                    Picasso.get().load(restaurants?.image).into(detailImageView)
                tvName.text = restaurants!!.name
            } else

                motionLayout.progress = motionProgress

            smoothScroller = object : LinearSmoothScroller(requireContext()) {
                override fun getVerticalSnapPreference(): Int {
                    return SNAP_TO_START
                }
            }
        }
    }

    fun blendColors(color1: Int, color2: Int, ratio: Float): Int {
        val inverseRatio = 1 - ratio
        val r = Color.red(color1) * ratio + Color.red(color2) * inverseRatio
        val g = Color.green(color1) * ratio + Color.green(color2) * inverseRatio
        val b = Color.blue(color1) * ratio + Color.blue(color2) * inverseRatio
        return Color.rgb(r.toInt(), g.toInt(), b.toInt())
    }

    private fun setupProductObserver() {
        launchAndRepeatWithViewLifecycle {
            viewModel.productState.collectLatest {
                when (it) {
                    is UiStateList.ERROR -> {
                        showToast(it.message)
                        binding.shimmerView.removeAllViews()
                        binding.recyclerView.setBackgroundResource(R.drawable.shape)
                    }

                    UiStateList.LOADING -> {
                        binding.shimmerView.startShimmer()
                        binding.recyclerView.setBackgroundResource(android.R.color.transparent)
                    }

                    is UiStateList.SUCCESS -> {
                        Log.d("TAG", "setupProductObserver: success ${it.data} ")
                        binding.shimmerView.removeAllViews()
                        binding.shimmerView.visibility = View.GONE
                        binding.recyclerView.visibility = View.VISIBLE
                        binding.recyclerView.setBackgroundResource(R.drawable.shape)
                        adapter.submitList(it.data.filter { it.product_type.isNotEmpty() })
                    }

                    else -> {
                    }
                }
            }
        }

        launchAndRepeatWithViewLifecycle {
            basketViewModel.basketProductsState.collect {
                when (it) {
                    is UiStateList.LOADING -> {
                    }

                    is UiStateList.SUCCESS -> {

                        Log.d("TAG", "setupBasketObserver: ${it.data.toString()} ")

                        if (it.data.isNotEmpty()) {
                            binding.basketLayout.isVisible = true
                            binding.basketLayout.invalidate()
                            basketViewModel.shopId = it.data.first().id
                            binding.count.text =
                                requireContext().resources.getQuantityString(
                                    R.plurals.product_count,
                                    it.data.count { it.count > 0 },
                                    it.data.count { it.count > 0 }
                                )
                        } else {
                            binding.basketLayout.isVisible = false
                            binding.basketLayout.invalidate()
                        }

                    }

                    is UiStateList.ERROR -> {
                    }

                    else -> {}
                }
            }
        }
    }

    private var lastClickTime = 0L
    private fun onItemClick(product: Product) {
        if (System.currentTimeMillis() - lastClickTime < 1000) return
        lastClickTime = System.currentTimeMillis()
        val dialog = ProductDetailsBottomSheet()
        val bundle = Bundle()
        bundle.putParcelable("product", product)
        bundle.putInt("restaurantId", restaurants!!.id)
        dialog.arguments = bundle
        dialog.onAddToBasket = { count ->
            addToBasket(product, count)
        }
        dialog.show(parentFragmentManager, null)
    }

//    private fun onCategoryClick(category: Category, position: Int) {
//        categoryAdapter.onSelectedItemChanged(position)
//        smoothScroller.targetPosition = position
//        (binding.recyclerView.layoutManager as LinearLayoutManager).startSmoothScroll(smoothScroller)
//        binding.motionLayout.progress = 1f
//    }

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

        Log.d("TAG", "addToBasket: ${basketViewModel.shopId} ")
        Log.d("TAG", "addToBasket: ${restaurants?.id} ")
//        if (basketViewModel.shopId != restaurants?.id && basketViewModel.basket.value.isNotEmpty()) {
//            val dialog = ShopsConfirmDialog(
//                title = getString(R.string.clear_basket),
//                message = getString(R.string.clear_basket_msg),
//                action = Action(getString(R.string.yes), true) {
//                    basketViewModel.clearBasket(restaurants?.id)
//                    Toast.makeText(requireContext(), "Savatga qo'shildi", Toast.LENGTH_SHORT).show()
//                    basketViewModel.getBasketProducts()
//                    basketViewModel.insertProductToBasket(selectedProduct)
//                },
//                secondAction = Action(getString(R.string.no), false) {}
//            )
//            dialog.show(parentFragmentManager, null)
//        } else {
            basketViewModel.shopId = restaurants!!.id
            basketViewModel.insertProductToBasket(selectedProduct)
            Toast.makeText(requireContext(), "Savatga qo'shildi", Toast.LENGTH_SHORT).show()
            basketViewModel.getBasketProducts()
        // }
    }

    override fun onPause() {
        super.onPause()
        motionProgress = binding.motionLayout.progress
    }
}

