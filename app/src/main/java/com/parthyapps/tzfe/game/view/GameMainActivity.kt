package com.parthyapps.tzfe.game.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.parthyapps.tzfe.game.R
import com.parthyapps.tzfe.game.databinding.ActivityGameMainBinding

class GameMainActivity : AppCompatActivity() {

    private var activityGameMainBinding: ActivityGameMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityGameMainBinding = ActivityGameMainBinding.inflate(layoutInflater)
    }
}