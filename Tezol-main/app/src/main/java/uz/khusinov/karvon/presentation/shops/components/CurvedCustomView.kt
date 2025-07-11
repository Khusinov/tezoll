package uz.khusinov.karvon.presentation.shops.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
//import org.jetbrains.anko.withAlpha
import uz.khusinov.karvon.R

class CurvedCustomView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {
    private val paint = Paint().apply {
        color = resources.getColor(R.color.white)
        style = Paint.Style.FILL
    }
    private val shadowPaint = Paint().apply {
        color = resources.getColor(R.color.white)
//        setShadowLayer(32f, 0f, 0f, Color.BLACK.withAlpha(16))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val height = height.toFloat()

        val path = Path().apply {
            moveTo(0f, 64f)
            quadTo(width / 2, 0f, width, 64f)
            lineTo(width, height)
            lineTo(0f, height)
            close()
        }

        // Draw shadow
        canvas.drawPath(path, shadowPaint)

        // Draw arc shape
        canvas.drawPath(path, paint)
    }
}