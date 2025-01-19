package com.ireddragonicy.nadma

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.ireddragonicy.nadma.databinding.FragmentAccountBinding

class AccountFragment : BaseFragment(R.layout.fragment_account) {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentAccountBinding.bind(view)

        binding.profileSection.setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}