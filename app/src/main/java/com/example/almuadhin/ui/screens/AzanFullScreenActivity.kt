package com.example.almuadhin.ui.screens

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.almuadhin.alarm.AzanMediaPlayer
import com.example.almuadhin.alarm.DismissReceiver
import com.example.almuadhin.databinding.ActivityAzanFullscreenBinding

class Activity : AppCompatActivity() {

    private lateinit var binding: ActivityAzanFullscreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAzanFullscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // تفتح على شاشة القفل
        window.addFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
            WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
            WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        )

        val prayerName = intent.getStringExtra("prayer_name") ?: ""
        binding.tvPrayerName.text = prayerName

        binding.btnDismiss.setOnClickListener {
            AzanMediaPlayer.player?.stop()
            AzanMediaPlayer.player?.release()
            AzanMediaPlayer.player = null
            finish()
        }
    }
}
