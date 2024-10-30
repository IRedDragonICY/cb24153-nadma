package com.ireddragonicy.nadma

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ireddragonicy.nadma.databinding.FragmentHomeBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val dateFormatDay = SimpleDateFormat("dd", Locale.getDefault())
    private val dateFormatMonth = SimpleDateFormat("MMM", Locale.getDefault())
    private val dateFormatYear = SimpleDateFormat("yyyy", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDynamicDate()
        setupButtonListeners()
    }

    private fun setDynamicDate() {
        val calendar = Calendar.getInstance()

        binding.headerHome.textDay.text = dateFormatDay.format(calendar.time)
        binding.headerHome.textMonth.text = dateFormatMonth.format(calendar.time).uppercase(Locale.getDefault())
        binding.headerHome.textYear.text = dateFormatYear.format(calendar.time)
    }

    private fun setupButtonListeners() {
        binding.quickAccess.btnEmergencyContact.setOnClickListener {
            val intent = Intent(requireContext(), EmergencyContactActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
