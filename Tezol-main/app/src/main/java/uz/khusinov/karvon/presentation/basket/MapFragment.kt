package uz.khusinov.karvon.presentation.basket

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.location.Location
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraBoundsOptions
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.EdgeInsets
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.PuckBearingSource
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.attribution.attribution
import com.mapbox.maps.plugin.compass.compass
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.location2
import com.mapbox.maps.plugin.logo.logo
import com.mapbox.maps.plugin.scalebar.scalebar
import com.vmadalin.easypermissions.EasyPermissions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.khusinov.karvon.R
import uz.khusinov.karvon.SharedPref
import uz.khusinov.karvon.databinding.FragmentMapBinding
import uz.khusinov.karvon.domain.model.CreateOrderRequest
import uz.khusinov.karvon.domain.model.DeliveryPrice
import uz.khusinov.karvon.domain.model.GetDeliveryPriceRequest
import uz.khusinov.karvon.domain.model.OrderItem
import uz.khusinov.karvon.presentation.BaseFragment
import uz.khusinov.karvon.presentation.home.HomeViewModel
import uz.khusinov.karvon.utills.Constants
import uz.khusinov.karvon.utills.launchAndRepeatWithViewLifecycle
import uz.khusinov.karvon.utills.UiStateList
import uz.khusinov.karvon.utills.UiStateObject
import uz.khusinov.karvon.utills.viewBinding
import javax.inject.Inject

@AndroidEntryPoint
class MapFragment : BaseFragment(R.layout.fragment_map) {
    private val binding by viewBinding { FragmentMapBinding.bind(it) }
    private lateinit var mapView: MapView
    private lateinit var map: MapboxMap
    private var locationPermissionGranted = false
    private val viewModel by viewModels<BasketViewModel>()
    private val homeViewModel by viewModels<HomeViewModel>()
    private var fromLatitude: Double = 0.0
    private var fromLongitude: Double = 0.0
    private var totalPrice: Double = 0.0
    private var findNameOfLocationJob: Job? = null
    private var placeName = ""
    private var lastLocation: Location? = null
    private lateinit var requestLocationFinePermissionLauncher: ActivityResultLauncher<String>
    private val productsList = mutableListOf<OrderItem>()
    private var isInsideCity = false
    private var shopId = 0
    private val locations = listOf(
        LatLng(41.75808338818937, 60.06495501894471),
        LatLng(41.79169743184209, 60.134075588298025),
        LatLng(41.75181994026841, 60.23675451020754),
        LatLng(41.75181994026841, 60.23675451020754),
        LatLng(41.732800712999804, 60.28873699491232),
        LatLng(41.79450423206247, 60.345771961585136),
        LatLng(41.730782680107666, 60.50462549454365),
        LatLng(41.58934438461457, 60.45267028306781),
        LatLng(41.63758595176404, 60.40074489538435),
        LatLng(41.603054449676314, 60.269760010875906)
    )

    @Inject
    lateinit var sharedPref: SharedPref


    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient


    private val onMoveListener = object : OnMoveListener {

        override fun onMoveBegin(detector: MoveGestureDetector) {
            binding.apply {

            }
        }

        override fun onMove(detector: MoveGestureDetector): Boolean {
            return false
        }

        override fun onMoveEnd(detector: MoveGestureDetector) {
            binding.apply {

            }

            fromLongitude = map.cameraState.center.longitude()
            fromLatitude = map.cameraState.center.latitude()
            isInsideCity = checkIsInsideCity(fromLatitude, fromLongitude)
            homeViewModel.isInsideCity.value = isInsideCity

            // when choosing "location from"
            findNameOfLocationJob?.cancel()
            findNameOfLocationJob = lifecycleScope.launch {
                delay(500)
                viewModel.getAddressNameFromOpenStreet(fromLatitude, fromLongitude)
            }
        }
    }

    private fun checkIsInsideCity(fromLatitude: Double, fromLongitude: Double): Boolean {
        return PolyUtil.containsLocation(fromLatitude, fromLongitude, locations, true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getBasketProducts()
        requestLocationFinePermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                locationPermissionGranted = true
                currentLocation(fromMapReady = true)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupObserver()
        setupObserver2()
        setupObserver3()
        if (!::mapView.isInitialized) {
            mapView = binding.mapView
            onMapReady(mapboxMap = mapView.getMapboxMap())
            currentLocation()
        }
    }

    private fun setupUI() = with(binding) {

        homeViewModel.isInsideCity.observe(viewLifecycleOwner) {
            if (it == true)
                select.isEnabled = true
            else
                select.isEnabled = true
        }


        back.setOnClickListener {
            findNavController().navigateUp()
        }

        currentLocation.setOnClickListener {
            currentLocation()
        }

        select.setOnClickListener {
            Log.d("TAG", "setupUI: 181 order create clicked ")
            val addressName = binding.addressName.text.toString()
            if (isInsideCity) {

                if (fromLongitude != 0.0 && fromLatitude != 0.0 && productsList.isNotEmpty() && addressName.isNotEmpty()) {
                    if (fromLatitude != 41.55837631225587 && fromLongitude != 60.622047424316406) {

                        viewModel.getDeliveryPrice(
                            GetDeliveryPriceRequest(
                                fromLatitude,
                                fromLongitude,
                                shopId
                            )
                        )
                    } else {
                        showToast("Yetkazish manzilini qayta tanlang")
                        currentLocation()
                    }
                } else showToast("Product yoki manzil tanlanmagan. Qayta urining!")
            } else {
                showToast("Xozircha bu manzilda faoliyat ko'rsatilmaydi")
            }
        }
    }


    private fun onMapReady(mapboxMap: MapboxMap) {
        val location = LatLng(sharedPref.latitude.toDouble(), sharedPref.longitude.toDouble())
        map = mapboxMap
        mapView.compass.enabled = false
        mapView.scalebar.enabled = false
        mapView.gestures.rotateEnabled = false
        mapView.gestures.scrollEnabled = true

        map.setCamera(
            CameraOptions.Builder().center(
                Point.fromLngLat(location.longitude, location.latitude)
            )
                .zoom(if (sharedPref.latitude == 41.841812f && sharedPref.longitude == 60.391438f) 12.0 else 15.0)
                .build()
        )

        val style = when (sharedPref.mode) {
            "night" -> Style.SATELLITE_STREETS
            "light" -> Style.SATELLITE_STREETS
            else -> {
                when (requireContext().resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                    Configuration.UI_MODE_NIGHT_YES -> Style.SATELLITE_STREETS
                    Configuration.UI_MODE_NIGHT_NO -> Style.SATELLITE_STREETS
                    else -> Style.SATELLITE_STREETS
                }
            }
        }

        map.loadStyle(
            (com.mapbox.maps.extension.style.style(styleUri = style) {
                mapView.gestures.addOnMoveListener(onMoveListener)
            })
        )

        mapView.location2.puckBearingSource = PuckBearingSource.HEADING
        mapView.location2.puckBearingSource = PuckBearingSource.COURSE
        map.setBounds(CameraBoundsOptions.Builder().maxZoom(18.0).minZoom(8.0).build())
        mapView.attribution.enabled = false
        mapView.logo.enabled = false

        fromLongitude = map.cameraState.center.longitude()
        fromLatitude = map.cameraState.center.latitude()
        viewModel.getAddressNameFromOpenStreet(fromLatitude, fromLongitude)
        isInsideCity = checkIsInsideCity(fromLatitude, fromLongitude)
        homeViewModel.isInsideCity.value = isInsideCity
    }

    private fun hasLocationPermissionFine() = EasyPermissions.hasPermissions(
        requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
    )

    @SuppressLint("MissingPermission")
    private fun currentLocation(fromMapReady: Boolean = false) {
        var hasPermission = hasLocationPermissionFine()
        if (!hasLocationPermissionFine()) {
            if (::requestLocationFinePermissionLauncher.isInitialized) {
                requestLocationFinePermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        } else if (isLocationEnabled(requireContext())) {
            if (lastLocation != null) {
                try {
                    viewModel.getAddressNameFromOpenStreet(
                        lastLocation!!.latitude, lastLocation!!.longitude
                    )
                } catch (_: Exception) {
                }
                flyTo(lastLocation!!.longitude, lastLocation!!.latitude, 15.0)
                fromLatitude = lastLocation!!.latitude
                fromLongitude = lastLocation!!.longitude
                mapView.setPadding(0, 0, 0, 0)
            } else if (fromMapReady) {
                fusedLocationProviderClient.getCurrentLocation(
                    LocationRequest.PRIORITY_HIGH_ACCURACY,
                    null
                ).addOnSuccessListener {
                    map.setCamera(
                        CameraOptions.Builder().center(Point.fromLngLat(it.longitude, it.latitude))
                            .build()
                    )
                    viewModel.getAddressNameFromOpenStreet(it.latitude, it.longitude)
                }
            } else {
                getLocation()
            }
        }
    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    lastLocation = location
                    sharedPref.latitude = location.latitude.toFloat()
                    sharedPref.longitude = location.longitude.toFloat()
                } else {
                    sharedPref.latitude = Constants.DEFAULT_LAT.toFloat()
                    sharedPref.longitude = Constants.DEFAULT_LON.toFloat()
                    lastLocation = Location("").apply {
                        latitude = Constants.DEFAULT_LAT
                        longitude = Constants.DEFAULT_LAT
                    }
                }
            }
        }
    }


    private fun setupObserver() {
        launchAndRepeatWithViewLifecycle {
            viewModel.addressNameFromOpenStreet.collect {
                when (it) {
                    is UiStateObject.LOADING -> {}

                    is UiStateObject.SUCCESS -> {
                        findNameOfLocationJob?.cancel()

                        placeName = if (it.data.address != null) {
                            it.data.address.amenity ?: it.data.address.road ?: it.data.address.city
                            ?: if (it.data.display_name.length > 32) it.data.display_name.substring(
                                0, 32
                            ) + "..." else it.data.display_name
                        } else if (it.data.name != null) if (it.data.name.length > 32) it.data.name.substring(
                            0,
                            32
                        ) + "..."
                        else it.data.name else ""

                        binding.addressName.text = placeName

                    }

                    is UiStateObject.ERROR -> {
                    }

                    else -> {
                    }
                }
            }
        }
    }

    private fun setupObserver3() {
        launchAndRepeatWithViewLifecycle {
            viewModel.basketProductsState.collect {
                when (it) {
                    is UiStateList.LOADING -> {
                    }

                    is UiStateList.SUCCESS -> {
                        val selectedShopId = viewModel.getSelectedShop().second
                        if (it.data.isNotEmpty()) {
                            totalPrice = 0.0
                            shopId = it.data.first().shop
                            productsList.clear()
                            for (i in it.data) {
                                if (i.shop == selectedShopId) {
                                    val productItem = OrderItem(0, 0)
                                    productItem.product_type = i.id
                                    productItem.quantity = i.selectedCount
                                    productsList.add(productItem)
                                }
                            }
                        }
                    }

                    is UiStateList.ERROR -> {
                    }

                    else -> {}
                }
            }
        }
    }

    private fun isLocationEnabled(context: Context): Boolean {
        val locationMode = try {
            Settings.Secure.getInt(context.contentResolver, Settings.Secure.LOCATION_MODE)
        } catch (e: Settings.SettingNotFoundException) {
            e.printStackTrace()
            return false
        }
        return locationMode != Settings.Secure.LOCATION_MODE_OFF
    }

    private fun setupObserver2() {
        // select location
        launchAndRepeatWithViewLifecycle {
            viewModel.deliveryPrice.collect {

                when (it) {
                    is UiStateObject.LOADING -> {
                        showProgress()
                    }

                    is UiStateObject.SUCCESS -> {
                        val bundle = Bundle()
                        bundle.putInt("standard", it.data.standard_price)
                        bundle.putInt("express", it.data.express_price)
                        bundle.putDouble("fromLatitude", fromLatitude)
                        bundle.putDouble("fromLongitude", fromLongitude)
                        bundle.putString("addressName", placeName)
                        findNavController().navigate(R.id.action_mapFragment_to_confirmFragment, bundle)
                        hideProgress()
                    }

                    is UiStateObject.ERROR -> {
                        hideProgress()
                        showToast(it.message)
                        Log.d("TAG", "setupObserver2: error  ${it.message} ")
                    }

                    else -> {
                        Log.d("TAG", "setupObserver2: ${it.toString()} ")
                    }
                }
            }
        }
    }

    private fun flyTo(longitude: Double, latitude: Double, zoom: Double) {
        map.flyTo(
            CameraOptions.Builder().center(
                Point.fromLngLat(
                    longitude, latitude
                )
            ).padding(EdgeInsets(0.0, 0.0, 0.0, 0.0)).zoom(zoom).build(),
            MapAnimationOptions.mapAnimationOptions {
                duration(1200)
            })
    }

}