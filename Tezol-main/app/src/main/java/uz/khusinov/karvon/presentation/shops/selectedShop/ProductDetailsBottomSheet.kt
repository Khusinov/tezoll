package uz.khusinov.karvon.presentation.shops.selectedShop

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import uz.khusinov.karvon.R
import uz.khusinov.karvon.databinding.BottomSheetProductDetailsBinding
import uz.khusinov.karvon.domain.model.shop.Product
import uz.khusinov.karvon.presentation.shops.ShopsViewModel
import uz.khusinov.karvon.utills.calculateDiscount
import uz.khusinov.karvon.utills.price
import uz.khusinov.karvon.utills.viewBinding

@AndroidEntryPoint
class ProductDetailsBottomSheet : BottomSheetDialogFragment(R.layout.bottom_sheet_product_details) {
    private val binding by viewBinding(BottomSheetProductDetailsBinding::bind)
    private val viewModel by activityViewModels<ShopsViewModel>()

    var onAddToBasket: (Int) -> Unit = { _ -> }

    private lateinit var product: Product
    private var count: Int = 1
    private var restaurantId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        product = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("product", Product::class.java)!!
        } else {
            arguments?.getParcelable("product")!!
        }
        restaurantId = arguments?.getInt("restaurantId") ?: -1
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
    }

    override fun getTheme() = R.style.CustomBottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            val bottomSheet =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                    ?: return@setOnShowListener
            BottomSheetBehavior.from(bottomSheet).apply {
                state = BottomSheetBehavior.STATE_EXPANDED
                skipCollapsed = true
            }
        }
        return dialog
    }

    private fun setupUi() = with(binding) {
        if (product.image != null)
            Picasso.get().load(product.image).into(productImage)
        else {
            Log.d("TAG", "setupUi: ${product} ")
            Log.d("TAG", "setupUi:product image null ")
        }
        productName.text = product.name
        productDescription.text = product.description
        productPrice.text = calculateDiscount(price = product.product_type.first().price, discount = product.product_type.first().discount, discountType = product.product_type.first().discount_type ).toString()
        productCount.text = count.toString()
        minus.setOnClickListener {
            if (count > 1) {
                count--
                productCount.text = count.toString()
            }
        }

        plus.setOnClickListener {
            count++
            productCount.text = count.toString()
        }

        addToBasket.setOnClickListener {
            onAddToBasket(count)
            dismiss()
        }
    }
}