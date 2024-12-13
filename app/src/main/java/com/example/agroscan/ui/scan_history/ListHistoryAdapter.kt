package com.example.agroscan.ui.scan_history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.agroscan.R
import com.example.agroscan.data.local.response.ScanHistoryItem
import com.example.agroscan.databinding.ItemHistoryScanBinding
import com.example.agroscan.ui.scan_history_detail.DetailHistoryFragment

class ListHistoryAdapter(
    private val historyList: List<ScanHistoryItem>,
    private val onClick: (ScanHistoryItem) -> Unit
) : RecyclerView.Adapter<ListHistoryAdapter.ListHistoryViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHistoryViewHolder {
        val binding = ItemHistoryScanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListHistoryViewHolder(binding)
    }

    override fun getItemCount(): Int = historyList.size

    override fun onBindViewHolder(holder: ListHistoryViewHolder, position: Int) {
        val item = historyList[position]
        holder.bind(item)
    }

    class ListHistoryViewHolder(private val binding: ItemHistoryScanBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ScanHistoryItem) {
            binding.ivHistoryScan.setImageResource(item.imageRes)
            binding.tvPlantName.text = item.plantName
            binding.tvHistoryScanResult.text = item.scanResult
            binding.tvHistoryScanDate.text = item.scanDate

            // handle item click
            itemView.setOnClickListener {
                val bundle = Bundle().apply {
                    putParcelable("scan_history_item", item) // passing data
                }

                // Move to detail fragment with FragmentTransaction
                itemView.findNavController().navigate(R.id.action_scan_history_to_detail_history, bundle)
            }
        }
    }
}