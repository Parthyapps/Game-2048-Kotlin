package com.parthyapps.tzfe.game.view

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

    override fun newGame(newHighScore: Int) {
        super.newGame(newHighScore)
        this.startLastGame = Date()
    }

    fun getTotalTimePlaying() = (Date().time - startedPlaying.time)
    fun getTimePlayingCurrent() = (Date().time - startLastGame.time)
}