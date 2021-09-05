package com.parthyapps.tzfe.game.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.Handler
import android.view.View
import com.parthyapps.tzfe.game.databinding.ActivityGameMainBinding
import com.parthyapps.tzfe.game.databinding.ActivitySplashBinding
import com.parthyapps.tzfe.game.view.GameMainActivity

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activitySplashBinding = ActivitySplashBinding.inflate(layoutInflater)
        val view: View = activitySplashBinding.root
        setContentView(view)
        Handler().postDelayed({
            val mainIntent = Intent(this@SplashScreen, GameMainActivity::class.java)
            startActivity(mainIntent)
            finish()
        }, 2000)
    }
}