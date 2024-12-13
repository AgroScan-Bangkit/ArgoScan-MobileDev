package com.example.agroscan.ui.scan_result

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.agroscan.R


class ScanResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_scan_result)


        val imageUrl = intent.getStringExtra("image_url")
        val prediction = intent.getStringExtra("prediction")
        val recommendedAction = intent.getStringExtra("recommended_action")

        val ivPredictionImage: ImageView = findViewById(R.id.iv_scan_result)
        val tvPrediction: TextView = findViewById(R.id.tv_result)
        val tvRecommendedAction: TextView = findViewById(R.id.tv_tindakan)

        tvPrediction.text = "Prediction: $prediction"
        tvRecommendedAction.text = "Recommended Action: $recommendedAction"

        Glide.with(this)
            .load(imageUrl)
            .transform(RoundedCorners(30))
            .into(ivPredictionImage)
    }
}
