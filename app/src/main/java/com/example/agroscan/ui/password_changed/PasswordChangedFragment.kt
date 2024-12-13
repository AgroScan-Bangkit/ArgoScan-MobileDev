package com.example.agroscan.ui.password_changed

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.agroscan.R
import com.example.agroscan.databinding.FragmentPasswordChangedBinding
import com.example.agroscan.ui.login.LoginActivity

class PasswordChangedFragment : Fragment() {

    private lateinit var binding: FragmentPasswordChangedBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPasswordChangedBinding.inflate(inflater, container, false)

        binding.btnGoToLogin.setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }

        return binding.root

    }

}