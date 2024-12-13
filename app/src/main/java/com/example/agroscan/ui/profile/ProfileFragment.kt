package com.example.agroscan.ui.profile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.agroscan.data.local.response.UserPreferences
import com.example.agroscan.databinding.FragmentProfileBinding
import com.example.agroscan.ui.login.LoginActivity
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private lateinit var userPreferences: UserPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        userPreferences = UserPreferences(requireContext())

        _binding?.tvLogout?.setOnClickListener {
            showLogoutDialog()
        }
        return _binding?.root
    }

    private fun showLogoutDialog() {
        // Membangun dialog
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Confirm Logout")
            .setMessage("Are you sure you want to logout?")
            .setCancelable(true)
            .setPositiveButton("Yes") { _, _ ->
                // Jika user klik Yes, lakukan logout
                logoutUser()
            }
            .setNegativeButton("No") { dialog, _ ->
                // Jika user klik No, tutup dialog
                dialog.dismiss()
            }
            .create()

        // Menampilkan dialog
        dialog.show()
    }

    // Fungsi untuk logout
    private fun logoutUser() {
        lifecycleScope.launch {
            // Hapus token pengguna atau data lainnya
            userPreferences.clearToken()
            // Pindahkan ke LoginActivity
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }
}
