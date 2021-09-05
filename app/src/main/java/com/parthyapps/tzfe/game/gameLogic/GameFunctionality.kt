package com.parthyapps.tzfe.game.gameLogic

import com.parthyapps.tzfe.game.utils.Constants
import java.io.Serializable

// Required protocol of delegate using this game engine
interface GameFunctionProtocol {
    fun userPB(score: Int)
    fun userWin()
    fun userFail()
    fun userScoreChanged(score: Int)
}

// Allowable game moves
enum class GameMoves { Up, Down, Left, Right }

// Allowable tile movement sequences
enum class TileMoveType { Add, Slide, Merge, Clear, Reset }

open class GameFunctionality(private var delegate: GameFunctionProtocol) : Serializable {

    private val gridCount = Constants.TILE_CNT
    private val rowCnt = Constants.DIMENSION
    private val colCnt = Constants.DIMENSION
    private val blankTile = Constants.EMPTY_TILE_VAL


}