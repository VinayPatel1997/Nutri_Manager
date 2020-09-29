package com.example.nutri_manager.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.nutri_manager.R
import com.example.nutri_manager.other.MapUtility
import com.example.nutri_manager.ui.FoodActivity
import com.example.nutri_manager.ui.FoodViewModel
import com.example.nutri_manager.util.Constants
import com.example.nutri_manager.util.Constants.Companion.REQUEST_CODE_LOCATION_PERMISSION
import com.example.nutri_manager.util.Resource
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.android.synthetic.main.fragment_search_food.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import kotlin.text.StringBuilder


class MapFragment : Fragment(R.layout.fragment_map), EasyPermissions.PermissionCallbacks {
    lateinit var foodViewModel: FoodViewModel
    private var map: GoogleMap? = null;
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient


    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        foodViewModel = (activity as FoodActivity).viewModel
        fusedLocationProviderClient = FusedLocationProviderClient(requireActivity());
        var currentLat: Double?
        var currentLng: Double?
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync {
            map = it
        }

        fabMap.setOnClickListener {
            requestPermissions()
            if (MapUtility.hasLocationPermission(requireContext())){
                fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
                    currentLat = location?.latitude
                    currentLng = location?.longitude
                    Toast.makeText(
                        context,
                        "lat $currentLat , lng $currentLng",
                        Toast.LENGTH_SHORT
                    ).show()

                    if (currentLng != null && currentLat != null) {
                        mapView.getMapAsync {
                            foodViewModel.getNearbyPlaces(getUrl(currentLat, currentLng))
                            foodViewModel.getNearbyPlaces.observe(
                                viewLifecycleOwner,
                                Observer { response ->
                                    when (response) {
                                        is Resource.Success -> {
                                            hideProgressBar()
                                            response.data?.let { mapResponse ->
//                        map!!.clear()
                                                Toast.makeText(
                                                    context,
                                                    "Map response ${mapResponse.status}, ${mapResponse.results.toList().size}",
                                                    Toast.LENGTH_SHORT
                                                ).show()

                                                for (nearbyPlace in mapResponse.results.toList()) {
                                                    val markerOptions = MarkerOptions()
                                                    val lat = nearbyPlace.geometry!!.location!!.lat
                                                    val lng = nearbyPlace.geometry!!.location!!.lng
                                                    val placeName = nearbyPlace.name
                                                    markerOptions.position(LatLng(lat, lng))
                                                    markerOptions.title(placeName)
                                                    map!!.addMarker(markerOptions)

                                                }
                                                map!!.moveCamera(
                                                    CameraUpdateFactory.newLatLng(
                                                        LatLng(
                                                            currentLat!!,
                                                            currentLng!!
                                                        )
                                                    )
                                                )
                                                map!!.animateCamera(CameraUpdateFactory.zoomTo(11f))
                                            }
                                        }
                                        is Resource.Error -> {
                                            hideProgressBar()
                                            response.message?.let { message ->
                                                Toast.makeText(
                                                    activity,
                                                    "An error occured: $message",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }
                                        }
                                        is Resource.Loading -> {
                                            Toast.makeText(
                                                requireContext(),
                                                "Loading",
                                                Toast.LENGTH_SHORT
                                            )
                                                .show()
                                            showProgressBar()
                                        }
                                    }
                                })
                        }
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Please turn on location and try again",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    private fun getUrl(lat: Double?, lng: Double?): String {
        return "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=$lat,$lng&radius=7000&type=hospital&key=AIzaSyCsrOHsDuL10F5ZBFLVX3bJ-WeQVHSRyfE"
    }

    private fun hideProgressBar() {
        paginationProgressBarMap.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        paginationProgressBarMap.visibility = View.VISIBLE
        isLoading = true
    }
    var isLoading = false

    private fun requestPermissions() {
        if (MapUtility.hasLocationPermission(requireContext())) {
            return
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept location permission to use this function of app",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept location permission to use this function of app",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermissions()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {}

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}
