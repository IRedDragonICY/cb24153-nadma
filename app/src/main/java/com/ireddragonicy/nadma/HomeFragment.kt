package com.ireddragonicy.nadma

import android.os.Bundle
import android.view.View
import com.ireddragonicy.nadma.databinding.FragmentMapsBinding

class HomeFragment : BaseFragment(R.layout.fragment_home) {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentMapsBinding.bind(view)
        // TODO: Implement your logic here
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
