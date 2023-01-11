package com.example.gpstracker

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.todo.base.BaseActivity
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task

class MainActivity : BaseActivity(), OnMapReadyCallback {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var map : SupportMapFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        map = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        map.getMapAsync(this)
        fusedLocationClient= LocationServices.getFusedLocationProviderClient(this)
        if(isLocationPermissionGranted()){
            showUserLocation()
        }
        else{
            requestLocationPermissionFromUser()
        }
    }
    ////// map
    var googleMap: GoogleMap?=null
    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap=googleMap
    }
    var userMarker : Marker?=null
    fun drawUserLocation(location: Location){
        val markerOptions= MarkerOptions()
            .position(LatLng(location.latitude,location.longitude))
        if (userMarker==null){
            userMarker=googleMap?.addMarker(markerOptions)
        }
        else{
            userMarker?.position= LatLng(location.latitude,location.longitude)
        }
        googleMap?.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
            LatLng(location.latitude,location.longitude),18f))

    }


////START TRACKING
    @SuppressLint("MissingPermission")
    private fun showUserLocation() {
        satisfySettingsToStartTracking()
        Toast.makeText(this,"showing user location",Toast.LENGTH_LONG).show()
    }

    val locationRequest = LocationRequest.create()?.apply {
        interval = 10000
        fastestInterval = 5000
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }
    private fun satisfySettingsToStartTracking() {
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest!!)

        val client: SettingsClient = LocationServices.getSettingsClient(this)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener { locationSettingsResponse ->
            startUserLocationTracking()
        }
        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException){
                try {
                    exception.startResolutionForResult(this@MainActivity,
                        LOCATION_SETTING_REQUEST_CODE)
                } catch (sendEx: IntentSender.SendIntentException) {
                }
            }
        }
    }
    val LOCATION_SETTING_REQUEST_CODE=200
    val locationCallback=object :LocationCallback(){
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            for (location in result.locations){

                drawUserLocation(location)

                Log.e("new location",""+location.latitude+""+location.longitude)
            }
        }
    }
    @SuppressLint("MissingPermission")
    fun startUserLocationTracking() {
        fusedLocationClient.requestLocationUpdates(locationRequest!!,locationCallback,
            Looper.getMainLooper())
    }

////RUN TIME PERMISSION
    val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                showUserLocation()
            } else {

                Toast.makeText(this,"can't find location because", Toast.LENGTH_LONG).show()
            }
        }
    private fun requestLocationPermissionFromUser() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
            showDialoge(message = " app needs to access your location",
                posActionName = "ok",
                posAction = { dialog, which ->
                    requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    dialog.dismiss()
                },
                negActionName = "no",
                negAction = { dialog, which ->
                    dialog.dismiss()
                })
        }
        else{
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED

    }

}