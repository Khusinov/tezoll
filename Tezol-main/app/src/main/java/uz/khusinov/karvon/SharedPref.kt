package uz.khusinov.karvon

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

class SharedPref @Inject constructor(context: Context) {

    private var mySharedPref: SharedPreferences =
        context.getSharedPreferences("filename", Context.MODE_PRIVATE)

    var language: Language
        get() = mySharedPref.getString("lang", Language.UZ.langCode)!!.toLanguage()
        set(value) {
            mySharedPref.edit().putString("lang", value.langCode).apply()
        }

    var languageState: Boolean
        get() = mySharedPref.getBoolean("langState", false)
        set(value) {
            mySharedPref.edit().putBoolean("langState", value).apply()
        }

    var mode: String
        get() = mySharedPref.getString("mode", "auto")!!
        set(value) = mySharedPref.edit().putString("mode", value).apply()

    var access: String
        get() = mySharedPref.getString("access", "")!!
        set(value) {
            mySharedPref.edit().putString("access", value).apply()
        }
    var refresh: String
        get() = mySharedPref.getString("refresh", "")!!
        set(value) {
            mySharedPref.edit().putString("refresh", value).apply()
        }

    var isEntered: Boolean
        get() = mySharedPref.getBoolean("entered", false)
        set(value) {
            mySharedPref.edit().putBoolean("entered", value).apply()
        }

    var isUpdate: Boolean
        get() = mySharedPref.getBoolean("update", false)
        set(value) {
            mySharedPref.edit().putBoolean("update", value).apply()
        }

    var googleNavigation: Boolean
        get() = mySharedPref.getBoolean("googleNavigation", false)
        set(value) {
            mySharedPref.edit().putBoolean("googleNavigation", value).apply()
        }

    var locationPermissionCoarse: Boolean
        get() = mySharedPref.getBoolean("locationPermissionCoarse", false)
        set(value) = mySharedPref.edit { putBoolean("locationPermissionCoarse", value) }

    var locationPermissionFine: Boolean
        get() = mySharedPref.getBoolean("locationPermissionFine", false)
        set(value) = mySharedPref.edit { putBoolean("locationPermissionFine", value) }

    var phoneCallPermission: Boolean
        get() = mySharedPref.getBoolean("phoneCallPermission", false)
        set(value) = mySharedPref.edit { putBoolean("phoneCallPermission", value) }

    var overlayPermission: Boolean
        get() = mySharedPref.getBoolean("overlayPermission", false)
        set(value) = mySharedPref.edit { putBoolean("overlayPermission", value) }

    var notificationPermission: Boolean
        get() = mySharedPref.getBoolean("notificationPermission", false)
        set(value) = mySharedPref.edit { putBoolean("notificationPermission", value) }

    var readExternalStoragePermission: Boolean
        get() = mySharedPref.getBoolean("readExternalStoragePermission", false)
        set(value) = mySharedPref.edit { putBoolean("readExternalStoragePermission", value) }

    var writeExternalStoragePermission: Boolean
        get() = mySharedPref.getBoolean("writeExternalStoragePermission", false)
        set(value) = mySharedPref.edit { putBoolean("writeExternalStoragePermission", value) }

    var allPermissionsGranted: Boolean
        get() = mySharedPref.getBoolean("allPermissionsGranted", false)
        set(value) = mySharedPref.edit { putBoolean("allPermissionsGranted", value) }

    var startChecked: Boolean
        get() = mySharedPref.getBoolean("startChecked", false)
        set(value) = mySharedPref.edit { putBoolean("startChecked", value) }

    var phone: String
        get() = mySharedPref.getString("phone", "")!!
        set(value) {
            mySharedPref.edit().putString("phone", value).apply()
        }

    var userId: String
        get() = mySharedPref.getString("userId", "")!!
        set(value) {
            mySharedPref.edit().putString("userId", value).apply()
        }

    var toBasket: Boolean
        get() = mySharedPref.getBoolean("toBasket", false)
        set(value) {
            mySharedPref.edit().putBoolean("toBasket", value).apply()
        }


    var latitude: Float
        get() = mySharedPref.getFloat("latitude", 41.558376f)
        set(value) = mySharedPref.edit { putFloat("latitude", value) }

    var longitude: Float
        get() = mySharedPref.getFloat("longitude", 60.622047f)
        set(value) = mySharedPref.edit { putFloat("longitude", value) }

    var selectedShopId: String
        get() = mySharedPref.getString("selectedShopId", "")!!
        set(value) {
            mySharedPref.edit().putString("selectedShopId", value).apply()
        }
}