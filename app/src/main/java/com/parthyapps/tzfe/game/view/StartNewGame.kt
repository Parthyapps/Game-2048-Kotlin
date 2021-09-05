package com.parthyapps.tzfe.game.view

import android.os.Build
import androidx.annotation.RequiresApi
import com.parthyapps.tzfe.game.gameLogic.GameFunctionality
import java.util.*

class StartNewGame(delegated: GameMainActivity) : GameFunctionality(delegate = delegated) {

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

    fun getTotalTimePlaying() = (Date().time - startedPlaying.time)
    fun getTimePlayingCurrent() = (Date().time - startLastGame.time)
}