package com.ireddragonicy.nadma

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ireddragonicy.nadma.databinding.FragmentHomeBinding
import com.ireddragonicy.nadma.services.LocationService
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.concurrent.thread

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var locationService: LocationService

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

        setDynamicContent()
        setupButtonListeners()
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

        thread {
            locationService.getCurrentLocation { location ->
                activity?.runOnUiThread {
                    binding.headerHome.locationText.text = location
                }
            }
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
