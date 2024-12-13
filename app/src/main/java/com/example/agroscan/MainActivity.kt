package com.example.agroscan

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.agroscan.data.local.response.UserPreferences
import com.example.agroscan.databinding.ActivityMainBinding
import com.example.agroscan.ui.home.HomeFragment
import com.example.agroscan.ui.login.LoginActivity
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        // Inisialisasi UserPreferences untuk akses DataStore
        userPreferences = UserPreferences.getInstance(this)

        // Cek apakah user sudah login
        MainScope().launch {
            val token = userPreferences.getToken()
            Log.d("UserPreferences", "Token: $token")

            if (token.isNullOrEmpty() || token == "null") {
                // Redirect ke LoginActivity
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                // Jika ada token, lanjutkan ke HomeFragment tanpa login ulang
                val navController = findNavController(R.id.nav_host_fragment_activity_main)
                // Atur title custom secara manual berdasarkan destination
                navController.navigate(R.id.navigation_home)
            }
        }

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val toolbar = findViewById<MaterialToolbar>(R.id.top_app_bar)
        setSupportActionBar(toolbar)

        // Disable back button
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_history, R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        // hubungkan toolbar dengan navigation controller
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_home -> {
                    toolbar.menu.clear()
                    toolbar.inflateMenu(R.menu.top_bar_menu)
                }

                R.id.navigation_history, R.id.navigation_profile -> {
                    toolbar.menu.clear()
                }

                else -> {
                    toolbar.menu.clear()
                }
            }
        }
    }
}