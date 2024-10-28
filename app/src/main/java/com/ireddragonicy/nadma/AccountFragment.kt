package com.ireddragonicy.nadma

import android.os.Bundle
import android.view.View
import com.ireddragonicy.nadma.databinding.FragmentAccountBinding

class AccountFragment : BaseFragment(R.layout.fragment_account) {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentAccountBinding.bind(view)
        // TODO: Implement your logic here
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
