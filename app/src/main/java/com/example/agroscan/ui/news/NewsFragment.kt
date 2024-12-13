package com.example.agroscan.ui.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.agroscan.MainActivity
import com.example.agroscan.R
import com.example.agroscan.data.local.response.ArticleItem
import com.example.agroscan.databinding.FragmentNewsBinding
import com.example.agroscan.ui.home.ListTopArticlesAdapter

class NewsFragment : Fragment() {

    private lateinit var binding: FragmentNewsBinding
    private lateinit var rvNews: RecyclerView
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentNewsBinding.inflate(inflater, container, false)

        // Atur ulang toolbar
        val activity = requireActivity() as AppCompatActivity
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(false)

        rvNews = binding.rvNews
        rvNews.layoutManager = LinearLayoutManager(requireContext())

        // sample data
        val newsArticles = listOf(
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

        newsAdapter = NewsAdapter(newsArticles)
        rvNews.adapter = newsAdapter

        return binding.root
    }
}