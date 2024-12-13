package com.example.agroscan.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.agroscan.R
import com.example.agroscan.data.local.response.ArticleItem
import com.example.agroscan.databinding.FragmentHomeBinding
import com.example.agroscan.ui.scan.ScanActivity
import com.example.agroscan.utils.getImageUri
import com.google.android.material.bottomsheet.BottomSheetDialog

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var currentImageUri: Uri? = null
    private lateinit var rvTopArticles: RecyclerView
    private lateinit var topArticlesAdapter: ListTopArticlesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.buttonScan.setOnClickListener { showBottomSheetDialog() }

        rvTopArticles = binding.rvTopArticles
        rvTopArticles.layoutManager = LinearLayoutManager(requireContext())

        // sample data
        val topArticles = listOf(
            ArticleItem(
                "5 Inovasi Terbaru dalam Teknologi Perkebunan untuk Peningkatan Hasil Panen",
                R.drawable.news2,
                "2023-08-01"
            ),
            ArticleItem(
                "Dampak Perubahan Iklim pada Komoditas Perkebunan: Strategi Adaptasi Petani",
                R.drawable.news3,
                "2023-08-02"
            ),
            ArticleItem(
                "Peluang Bisnis di Sektor Perkebunan Organik yang Sedang Berkembang Pesat",
                R.drawable.news2,
                "2023-08-03"
            ),
            ArticleItem(
                "Mengenal Jenis-Jenis Tanaman Perkebunan yang Paling Menguntungkan di Indonesia",
                R.drawable.daun_singkong,
                "2023-08-04"
            ),
            ArticleItem(
                "Panduan Lengkap untuk Mengoptimalkan Perkebunan Kopi dengan Teknik Modern",
                R.drawable.daun_jagung,
                "2023-08-05"
            )
        )

        topArticlesAdapter = ListTopArticlesAdapter(topArticles)
        rvTopArticles.adapter = topArticlesAdapter

        return root
    }

    // Bottom sheet dialog
    private fun showBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.CustomBottomSheetDialog)
        val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_camera_picker, null)


        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet =
                (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.background = requireContext().getDrawable(R.drawable.bottom_sheet_background)
        }

        bottomSheetDialog.setContentView(bottomSheetView)

        // Take reference to the button sheet
        val btnCamera = bottomSheetView.findViewById<ImageView>(R.id.ivCamera)
        val btnGallery = bottomSheetView.findViewById<ImageView>(R.id.ivGallery)
        val btnCancel = bottomSheetView.findViewById<TextView>(R.id.tvCancel)

        // Set button click listener
        btnCamera.setOnClickListener {
            startCamera()
            bottomSheetDialog.dismiss()
        }
        btnGallery.setOnClickListener {
            startGallery()
            bottomSheetDialog.dismiss()
        }
        btnCancel.setOnClickListener { bottomSheetDialog.dismiss() }

        bottomSheetDialog.show()
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this.requireContext())
        launcherIntentCamera.launch(currentImageUri)
    }

    // Membuka galeri untuk memilih gambar
    private fun startGallery() {
        launcherIntentGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherIntentCamera = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        if (isSuccess && currentImageUri != null) {
            val intent = Intent(requireContext(), ScanActivity::class.java)
            intent.putExtra("imageUri", currentImageUri.toString()) // Passing the URI to ScanActivity
            startActivity(intent)
        } else {
            currentImageUri = null
            Toast.makeText(requireContext(), "Gagal mengambil gambar, silahkan coba lagi!", Toast.LENGTH_SHORT).show()
        }
    }

    private val launcherIntentGallery =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            if (uri != null) {
                currentImageUri = uri
                val intent = Intent(requireContext(), ScanActivity::class.java)
                intent.putExtra("imageUri", currentImageUri.toString()) // Passing the URI to ScanActivity
                startActivity(intent)
            } else {
                currentImageUri = null
            }
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
