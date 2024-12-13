package com.example.agroscan.ui.scan_history_detail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.agroscan.R
import com.example.agroscan.data.local.response.ScanHistoryItem
import com.example.agroscan.databinding.FragmentDetailHistoryBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

@Suppress("DEPRECATION")
class DetailHistoryFragment : Fragment() {

    private lateinit var binding: FragmentDetailHistoryBinding
    private lateinit var scanHistoryItem: ScanHistoryItem

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDetailHistoryBinding.inflate(inflater, container, false)

        // get data from bundle
        arguments?.getParcelable<ScanHistoryItem>("scan_history_item")?.let {
            scanHistoryItem = it
            Log.d("DetailHistoryFragment", "scanHistoryItem: $scanHistoryItem")
        }

        // set data to view
        binding.scanResultDetail.text = scanHistoryItem.plantName
        binding.tvActionDetailDesc.text = scanHistoryItem.scanResult

        // set image to view
        Glide.with(requireContext())
            .load(scanHistoryItem.imageRes)
            .transform(RoundedCorners(30))
            .fitCenter()
            .into(binding.ivImageDetail)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        // set action bar title
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Detail Scan"
        // hide bottom navigation
        activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.visibility = View.GONE
        // back button
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onStop() {
        super.onStop()
        // show bottom navigation
        activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.visibility = View.VISIBLE
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // Handle the back button press
                Log.d("DetailHistoryFragment", "Back button pressed")
                findNavController().navigateUp()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }

    }
}