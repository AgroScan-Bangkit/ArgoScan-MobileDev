package com.example.agroscan.ui.news_detail

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.agroscan.R
import com.example.agroscan.databinding.ActivityDetailNewsBinding

class DetailNewsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailNewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val image = intent.getStringExtra("image") // Jika image adalah URL
        val imageResId = intent.getIntExtra("image", -1) // Jika image adalah resource ID
        val title = intent.getStringExtra("title")
        val date = intent.getStringExtra("date")

// Set data ke view
        binding.tvNewsTitleDetail.text = title
        binding.tvNewsDateDetail.text = date

// Set image
        if (image != null) {
            Glide.with(this)
                .load(image) // Untuk URL
                .transform(RoundedCorners(30))
                .fitCenter()
                .into(binding.ivNewsDetail)
        } else if (imageResId != -1) {
            Glide.with(this)
                .load(imageResId) // Untuk resource ID
                .transform(RoundedCorners(30))
                .fitCenter()
                .into(binding.ivNewsDetail)
        }

    }
}