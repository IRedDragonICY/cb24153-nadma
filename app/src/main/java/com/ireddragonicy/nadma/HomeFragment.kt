package com.ireddragonicy.nadma

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.ireddragonicy.nadma.databinding.FragmentHomeBinding
import com.ireddragonicy.nadma.services.LocationService
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var locationService: LocationService
    private lateinit var sessionManager: SessionManager

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                fetchLocation()
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                fetchLocation()
            }
            else -> {
                updateLocationText("Location permission denied")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        locationService = LocationService(requireContext())
        sessionManager = SessionManager(requireContext())

        setupGreeting()
        setDynamicContent()
        setupButtonListeners()
        checkLocationPermissionAndFetch()
    }

    private fun setupGreeting() {
        val greeting = if (sessionManager.isLoggedIn) {
            val userName = sessionManager.userName
            "Hello, $userName!"
        } else {
            "Hello, User!"
        }
        binding.headerHome.helloUser.text = greeting
    }

    private fun setDynamicContent() {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val dateParts = dateFormat.format(calendar.time).split(" ")

        binding.headerHome.apply {
            textDay.text = dateParts[0]
            textMonth.text = dateParts[1].uppercase(Locale.getDefault())
            textYear.text = dateParts[2]
        }
    }

    private fun setupButtonListeners() {
        binding.quickAccess.btnEmergencyContact.setOnClickListener {
            startActivity(Intent(requireContext(), EmergencyContactActivity::class.java))
        }

        binding.quickAccess.btnReportIncident.setOnClickListener {
            startActivity(Intent(requireContext(), ReportIncidentActivity::class.java))
        }
    }

    private fun checkLocationPermissionAndFetch() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                fetchLocation()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) || shouldShowRequestPermissionRationale(
                Manifest.permission.ACCESS_COARSE_LOCATION) -> {
                binding.headerHome.locationText.text = "Location permission required. Please grant it."
                binding.headerHome.locationText.setOnClickListener {
                    requestLocationPermission()
                    binding.headerHome.locationText.setOnClickListener(null)
                }
            }
            else -> {
                requestLocationPermission()
            }
        }
    }

    private fun requestLocationPermission() {
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    private fun fetchLocation() {
        locationService.getCurrentLocation { location ->
            activity?.runOnUiThread {
                updateLocationText(location)
            }
        }
    }

    private fun updateLocationText(location: String) {
        binding.headerHome.locationText.text = location
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}