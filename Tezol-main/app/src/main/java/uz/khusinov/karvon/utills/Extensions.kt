package uz.khusinov.karvon.utills

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.icu.text.DecimalFormat
import android.icu.text.DecimalFormatSymbols
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import uz.khusinov.karvon.domain.model.shop.Discount
import java.io.IOException


fun Throwable.userMessage() = when (this) {
    is HttpException -> when (this.code()) {
        304 -> "304 Not Modified"
        400 -> "400 Bad Request"
        401 -> "401 Unauthorized"
        403 -> "403 Forbidden"
        404 -> "404 Not Found"
        405 -> "405 Method Not Allowed"
        409 -> "User не найден"
        422 -> "422 Unprocessable"
        500 -> "500 Server Error"
        else -> "Something went wrong"
    }
    is IOException -> "Internet bilan aloqa yo'q"
    is JsonSyntaxException -> "Ma'lumot olishda xatolik"
    else -> this.localizedMessage
}!!

inline fun Fragment.launchAndRepeatWithViewLifecycle(
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline block: suspend CoroutineScope.() -> Unit,
) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.lifecycle.repeatOnLifecycle(minActiveState) {
            block()
        }
    }
}

@SuppressLint("NewApi")
fun price(price: Int): String {
    val decimalFormatSymbols = DecimalFormatSymbols()
    decimalFormatSymbols.groupingSeparator = ' '
    val decimalFormat = DecimalFormat("###,###", decimalFormatSymbols)
    return decimalFormat.format(price)
}

fun calculateDiscount(price:Int, discount: Discount? = null, discountType: String? = null): Int {
    if (discount == null || discountType == null || discount.discount_in_percent == null || discount.discount == null) {
        return price
    }

    return when (discountType) {
        "in_persent" -> (price * (1 - (discount.discount.toDoubleOrNull() ?: 0.0 ) / 100.0)).toInt()
        "in_price" -> (price - if (discount.discount.toDoubleOrNull() != null) discount.discount.toDouble() else 0.0).toInt()
        else -> {
            price
        }
    }
}

// Extensions.kt
fun String.formatPhoneNumber(): String {
    val digits = this.filter { it.isDigit() }
    val formattedNumber = buildString {
        append("+")

        if (digits.length >= 3) {
            append(digits.substring(0, 3))
            if (digits.length >= 5) {
                append(" ${digits.substring(3, 5)}")
                if (digits.length >= 8) {
                    append(" ${digits.substring(5, 8)}")
                    if (digits.length >= 10) {
                        append(" ${digits.substring(8, 10)}")
                        if (digits.length >= 12) {
                            append(" ${digits.substring(10, 12)}")
                        }
                    }
                } else { // Keyinchi shart qo'shildi
                    append(" ${digits.substring(8)}")
                }
            }
        }
    }
    return formattedNumber
}
fun String.extractLastThreeDigitsWithSpace(): String {
    val reversedString = this.reversed()
    val extractedDigits = reversedString.chunked(3)
        .map { it.reversed() }
        .reversed()
    return extractedDigits.joinToString(" ")
}

fun Fragment.getScreenHeight(): Int {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val metrics = requireActivity().windowManager.currentWindowMetrics
        return metrics.bounds.height()
    } else {
        val metrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(metrics)
        return metrics.heightPixels
    }
}

fun Fragment.getBitmapFromVectorDrawable(@DrawableRes drawableId: Int): Bitmap {
    val drawable = ContextCompat.getDrawable(requireContext(), drawableId)
    val bitmap = Bitmap.createBitmap(
        drawable!!.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}

fun Context.dp2px(dpValue: Float): Float {
    val scale = resources.displayMetrics.density
    return dpValue * scale + 0.5f
}

fun NavController.navigateSafe(@IdRes resId: Int, args: Bundle? = null) {
    val destinationId = currentDestination?.getAction(resId)?.destinationId.orEmpty()
    currentDestination?.let { node ->
        val currentNode = when (node) {
            is NavGraph -> node
            else -> node.parent
        }
        if (destinationId != 0) {
            currentNode?.findNode(destinationId)?.let { navigate(resId, args) }
        }
    }
}
fun Int?.orEmpty(default: Int = 0): Int {
    return this ?: default
}