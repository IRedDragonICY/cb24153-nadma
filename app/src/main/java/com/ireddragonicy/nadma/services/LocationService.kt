package com.ireddragonicy.nadma.services

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.Address
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.Locale

class LocationService(private val context: Context) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    fun getCurrentLocation(onResult: (String) -> Unit) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            onResult("Click here to give permission")
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                getAddressFromLocation(location) { address ->
                    onResult(address)
                }
            } else {
                onResult("Unable to retrieve location")
            }
        }
    }

    private fun getAddressFromLocation(location: Location, onResult: (String) -> Unit) {
        val geocoder = Geocoder(context, Locale.getDefault())

        geocoder.getFromLocation(
            location.latitude,
            location.longitude,
            1,
            object : Geocoder.GeocodeListener {
                override fun onGeocode(addresses: List<Address>) {
                    if (addresses.isNotEmpty()) {
                        val address = addresses[0]
                        val addressText = "${address.locality ?: "Unknown City"}, ${address.adminArea ?: "Unknown Province"}"
                        onResult(addressText)
                    } else {
                        onResult("Location not found")
                    }
                }

                override fun onError(errorMessage: String?) {
                    onResult(errorMessage ?: "Error retrieving address")
                }
            }
        )
    }
}
