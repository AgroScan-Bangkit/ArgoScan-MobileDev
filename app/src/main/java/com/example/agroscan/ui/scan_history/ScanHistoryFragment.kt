package com.example.agroscan.ui.scan_history

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agroscan.R
import com.example.agroscan.data.local.response.ScanHistoryItem
import com.example.agroscan.databinding.FragmentScanHistoryBinding

class ScanHistoryFragment : Fragment() {

    private lateinit var binding: FragmentScanHistoryBinding
    private lateinit var adapter: ListHistoryAdapter

    // Data Statis
    private val historyList = listOf(
        ScanHistoryItem(R.drawable.daun_jagung, "Jagung", "Penyakit Jagung", "20-01-2022"),
        ScanHistoryItem(R.drawable.carrot, "Wortel", "Penyakit Wortel", "20-01-2022"),
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScanHistoryBinding.inflate(inflater, container, false)

        // setup adapter
        adapter = ListHistoryAdapter(historyList) { item ->
            // handle item click
            Log.d("ScanHistoryFragment", "Item clicked: $item")
        }

        // setup recyclerview
        binding.rvHistoryScan.layoutManager = LinearLayoutManager(context)
        binding.rvHistoryScan.adapter = adapter

        return binding.root
    }
}