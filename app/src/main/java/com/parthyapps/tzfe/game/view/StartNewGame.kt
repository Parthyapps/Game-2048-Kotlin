package com.parthyapps.tzfe.game.view

import android.os.Build
import androidx.annotation.RequiresApi
import com.parthyapps.tzfe.game.gameLogic.GameFunctionProtocol
import com.parthyapps.tzfe.game.gameLogic.GameFunctionality
import java.util.*

class StartNewGame(delegated: GameFunctionProtocol) : GameFunctionality(delegate = delegated) {

    lateinit var startedPlaying: Date
        private set
    lateinit var startLastGame: Date
        private set

    init {
        this.startedPlaying = Date()
        this.startLastGame = this.startedPlaying
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun newGame(newHighScore: Int) {
        super.newGame(newHighScore)
        this.startLastGame = Date()
    }

}