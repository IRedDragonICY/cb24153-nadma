package com.ireddragonicy.nadma

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.ireddragonicy.nadma.databinding.FragmentAccountBinding

class AccountFragment : Fragment(R.layout.fragment_account) {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!
    private val sessionManager by lazy { SessionManager(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAccountBinding.bind(view)
        updateUI()

        binding.profileSection.setOnClickListener {
            if (sessionManager.isLoggedIn()) {
                sessionManager.signOut {
                    updateUI()
                }
            } else {
                startActivity(Intent(requireContext(), LoginActivity::class.java))
            }
        }
    }

    private fun updateUI() {
        if (sessionManager.isLoggedIn()) {
            val account = sessionManager.getSignedInAccount()
            binding.helloUserText.text = "Hello, ${account?.displayName ?: "User"}"
            binding.signinMessage.text = "Tap to sign out"
        } else {
            binding.helloUserText.text = "Hello User!"
            binding.signinMessage.text = "Sign in for better personalization"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}