package com.ireddragonicy.nadma.services

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.provider.Settings
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import java.util.Locale

class LocationService(private val context: Context) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private var locationCallback: LocationCallback? = null

    fun getCurrentLocation(onResult: (String) -> Unit) {
        if (!isLocationEnabled()) {
            onResult("Enable location services")
            context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            })
            return
        }

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            onResult("Location permission required")
            return
        }

        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            null
        ).addOnSuccessListener { location: Location? ->
            if (location != null) {
                getAddressFromLocation(location) { address ->
                    onResult(address)
                }
            } else {
                requestNewLocation(onResult)
            }
        }.addOnFailureListener {
            onResult("Failed to get location: ${it.message}")
        }
    }

    private fun requestNewLocation(onResult: (String) -> Unit) {
        val locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = Priority.PRIORITY_HIGH_ACCURACY
            numUpdates = 1
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    getAddressFromLocation(location, onResult)
                } ?: run {
                    onResult("Location unavailable")
                }
                locationCallback?.let { fusedLocationClient.removeLocationUpdates(it) }
            }
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback!!,
            Looper.getMainLooper()
        )
    }

    private fun getAddressFromLocation(location: Location, onResult: (String) -> Unit) {
        try {
            val geocoder = Geocoder(context, Locale.getDefault())

            geocoder.getFromLocation(
                location.latitude,
                location.longitude,
                1
            ) { addresses ->
                if (addresses.isNotEmpty()) {
                    val address = addresses[0]
                    val addressText = buildString {
                        append(address.locality ?: "Unknown City")
                        append(", ")
                        append(address.adminArea ?: "Unknown Province")
                    }
                    onResult(addressText)
                } else {
                    onResult("No address found")
                }
            }
        } catch (e: Exception) {
            onResult("Geocoder error: ${e.message}")
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
               locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
}