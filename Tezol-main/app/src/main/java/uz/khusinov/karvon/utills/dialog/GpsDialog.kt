package uz.khusinov.karvon.utills.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import uz.khusinov.karvon.R

@SuppressLint("InflateParams")
class GpsDialog(context: Context, isCancelable: Boolean = false) : Dialog(context) {
    var gpsText: TextView

    init {
        val wlmp = window!!.attributes
        wlmp.gravity = Gravity.CENTER_HORIZONTAL
        window!!.attributes = wlmp
        setTitle(null)
        setCancelable(isCancelable)
        setOnCancelListener(null)
        val view: View = LayoutInflater.from(context).inflate(
            R.layout.gps_layout, null
        )
        gpsText = view.findViewById(R.id.gpsText)
        setContentView(view)
    }
}