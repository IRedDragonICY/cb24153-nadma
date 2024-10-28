package com.ireddragonicy.nadma

import android.os.Bundle
import android.view.View
import com.ireddragonicy.nadma.databinding.FragmentNotificationBinding

class NotificationFragment : BaseFragment(R.layout.fragment_notification) {

    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentNotificationBinding.bind(view)
        // TODO: Implement your logic here
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
