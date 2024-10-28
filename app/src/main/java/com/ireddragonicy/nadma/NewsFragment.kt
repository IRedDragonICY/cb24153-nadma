package com.ireddragonicy.nadma

import android.os.Bundle
import android.view.View
import com.ireddragonicy.nadma.databinding.FragmentNewsBinding

class NewsFragment : BaseFragment(R.layout.fragment_news) {

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentNewsBinding.bind(view)
        // TODO: Implement your logic here
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
