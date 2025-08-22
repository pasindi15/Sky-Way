package com.example.skyway

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        
        // Hide action bar
        supportActionBar?.hide()

        val logo: ImageView = findViewById(R.id.finalLogo)
        // Simple fade-in then navigate (plane/clouds removed)
        logo.alpha = 0f
        logo.animate().alpha(1f).setDuration(800L).withEndAction {
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this@SplashActivity, GetStartedActivity::class.java))
                finish()
            }, 1500L)
        }.start()
    }
}
