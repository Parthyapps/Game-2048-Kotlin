package com.parthyapps.tzfe.game.gameLogic

import android.os.Build
import androidx.annotation.RequiresApi
import com.parthyapps.tzfe.game.utils.Constants
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

// Required protocol of delegate using this game engine
interface GameFunctionProtocol {
    fun updateTileValue(move: GameFunctionality.Transition)
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

    private var score = 0
    private var previousHighScore = 0
    var maxTile = Constants.EMPTY_TILE_VAL
        private set

    private var gameOver = false
    private var numEmpty = Constants.TILE_CNT

    private var previousMoves: ArrayList<GameBoardRecord> = ArrayList()
    private var transitions: ArrayList<Transition> = ArrayList()
    private var tiles: ArrayList<Int> = ArrayList()

    private val rand = Random()

    // Reset the playing board, and generate rendering transition records
    @RequiresApi(Build.VERSION_CODES.M)
    open fun newGame(newHighScore: Int) {
        this.previousHighScore = newHighScore
        this.numEmpty = Constants.TILE_CNT
        this.maxTile = Constants.EMPTY_TILE_VAL
        this.gameOver = false
        this.score = 0

        this.previousMoves = ArrayList()
        this.transitions = ArrayList()
        this.tiles = ArrayList()

        // Create clean array of tiles, and associated transitions
        for (i in 0 until gridCount) {
            tiles.add(Constants.EMPTY_TILE_VAL)
            this.transitions.add(Transition(TileMoveType.Clear, this.blankTile, i))
        }

        this.addNewTile(2)
        this.addNewTile(2)
        this.previousMoves.add(0, getGameBoardRecord())
        this.applyGameMoves()
    }

    // RePlot the game board to an earlier time.
    @RequiresApi(Build.VERSION_CODES.M)
    fun replotBoard() {
        transitions = ArrayList()
        for (i in 0 until gridCount) {
            this.transitions.add(Transition(TileMoveType.Reset, tiles[i], i))
        }
        this.applyGameMoves()
    }

    // Create and return a current game board status record object
    private fun getGameBoardRecord(): GameBoardRecord {
        return GameBoardRecord(gameOver, maxTile)
    }

    private fun addNewTile(seedValue: Int = -1): Boolean {
        if (numEmpty == 0) {
            return false
        }

        val value = if (seedValue < 2 || seedValue > 4) {
            // Randomly select 2 or 4 at a ratio 3.5:1 in favour ot 2.
            val sample = rand.nextInt(100)
            if (sample >= Constants.RANDOM_RATIO) {
                4
            } else {
                2
            }
        } else {
            seedValue
        }

        val pos = rand.nextInt(numEmpty)
        var blanksFound = 0

        for (i in 0 until gridCount) {
            if (tiles[i] == blankTile) {
                if (blanksFound == pos) {
                    tiles[i] = value
                    if (value > maxTile) {
                        maxTile = value
                    }
                    numEmpty -= 1
                    transitions.add(Transition(TileMoveType.Add, value, i))
                    return true
                }
                blanksFound += 1
            }
        }
        return false
    }

    private fun hasMovesRemaining(): Boolean {

        if (numEmpty > 0) {
            return true
        }

        // check left-right for compact moves remaining.
        val arrLimitX = gridCount - colCnt
        for (i in 0 until arrLimitX) {
            if (tiles[i] == tiles[i + colCnt]) {
                return true
            }
        }

        // check up-down for compact moves remaining.
        val arrLimitY = gridCount - 1
        for (i in 0 until arrLimitY) {
            if ((i + 1) % rowCnt > 0) {
                if (tiles[i] == tiles[i + 1]) {
                    return true
                }
            }
        }
        return false
    }

    private fun slideTileRowOrColumn(vararg indexes: Int): Boolean {

        var moved = false
        val tmpArr = intArrayOf(*indexes)

        // Do we have some sliding to do, or not?
        var es = 0  // empty spot index
        for (j in tmpArr.indices) {
            if (tiles[tmpArr[es]] != blankTile) {
                es += 1
                continue
            } else if (tiles[tmpArr[j]] == blankTile) {
                continue
            } else {
                // Otherwise we have a slide condition
                tiles[tmpArr[es]] = tiles[tmpArr[j]]
                tiles[tmpArr[j]] = blankTile
                transitions.add(
                    Transition(
                        TileMoveType.Slide,
                        tiles[tmpArr[es]],
                        tmpArr[es],
                        tmpArr[j]
                    )
                )
                moved = true
                es += 1
            }
        }
        return moved
    }

    private fun compactTileRowOrColumn(vararg indexes: Int): Boolean {

        var compacted = false
        val tmpArr = intArrayOf(*indexes)

        for (j in 0 until (tmpArr.size - 1)) {
            if (tiles[tmpArr[j]] != blankTile && tiles[tmpArr[j]] == tiles[tmpArr[j + 1]]) { // we found a matching pair
                val ctv = tiles[tmpArr[j]] * 2   // = compacted tile value
                tiles[tmpArr[j]] = ctv
                tiles[tmpArr[j + 1]] = blankTile
                score += ctv
                if (ctv > maxTile) {
                    maxTile = ctv
                }  // is this the biggest tile # so far
                transitions.add(Transition(TileMoveType.Merge, ctv, tmpArr[j], tmpArr[j + 1]))
                compacted = true
                numEmpty += 1
            }
        }
        return compacted
    }

    private fun slideLeft(): Boolean {
        val a = slideTileRowOrColumn(0, 4, 8, 12)
        val b = slideTileRowOrColumn(1, 5, 9, 13)
        val c = slideTileRowOrColumn(2, 6, 10, 14)
        val d = slideTileRowOrColumn(3, 7, 11, 15)
        return (a || b || c || d)
    }

    private fun slideRight(): Boolean {
        val a = slideTileRowOrColumn(12, 8, 4, 0)
        val b = slideTileRowOrColumn(13, 9, 5, 1)
        val c = slideTileRowOrColumn(14, 10, 6, 2)
        val d = slideTileRowOrColumn(15, 11, 7, 3)
        return (a || b || c || d)
    }

    private fun slideUp(): Boolean {
        val a = slideTileRowOrColumn(0, 1, 2, 3)
        val b = slideTileRowOrColumn(4, 5, 6, 7)
        val c = slideTileRowOrColumn(8, 9, 10, 11)
        val d = slideTileRowOrColumn(12, 13, 14, 15)
        return (a || b || c || d)
    }

    private fun slideDown(): Boolean {
        val a = slideTileRowOrColumn(3, 2, 1, 0)
        val b = slideTileRowOrColumn(7, 6, 5, 4)
        val c = slideTileRowOrColumn(11, 10, 9, 8)
        val d = slideTileRowOrColumn(15, 14, 13, 12)
        return (a || b || c || d)
    }

    private fun compactLeft(): Boolean {
        val a = compactTileRowOrColumn(0, 4, 8, 12)
        val b = compactTileRowOrColumn(1, 5, 9, 13)
        val c = compactTileRowOrColumn(2, 6, 10, 14)
        val d = compactTileRowOrColumn(3, 7, 11, 15)
        return (a || b || c || d)
    }

    private fun compactRight(): Boolean {
        val a = compactTileRowOrColumn(12, 8, 4, 0)
        val b = compactTileRowOrColumn(13, 9, 5, 1)
        val c = compactTileRowOrColumn(14, 10, 6, 2)
        val d = compactTileRowOrColumn(15, 11, 7, 3)
        return (a || b || c || d)
    }

    private fun compactUp(): Boolean {
        val a = compactTileRowOrColumn(0, 1, 2, 3)
        val b = compactTileRowOrColumn(4, 5, 6, 7)
        val c = compactTileRowOrColumn(8, 9, 10, 11)
        val d = compactTileRowOrColumn(12, 13, 14, 15)
        return (a || b || c || d)
    }

    private fun compactDown(): Boolean {
        val a = compactTileRowOrColumn(3, 2, 1, 0)
        val b = compactTileRowOrColumn(7, 6, 5, 4)
        val c = compactTileRowOrColumn(11, 10, 9, 8)
        val d = compactTileRowOrColumn(15, 14, 13, 12)
        return (a || b || c || d)
    }

    private fun actionMoveLeft(): Boolean {
        val a = slideLeft()
        val b = compactLeft()
        val c = slideLeft()
        return (a || b || c)
    }

    private fun actionMoveRight(): Boolean {
        val a = slideRight()
        val b = compactRight()
        val c = slideRight()
        return (a || b || c)
    }

    private fun actionMoveUp(): Boolean {
        val a = slideUp()
        val b = compactUp()
        val c = slideUp()
        return (a || b || c)
    }

    private fun actionMoveDown(): Boolean {
        val a = slideDown()
        val b = compactDown()
        val c = slideDown()
        return (a || b || c)
    }

    // THIS FUNCTION IS THE MAIN CONTROLLER FOR GAME MOVES
    @RequiresApi(Build.VERSION_CODES.M)
    fun actionMove(move: GameMoves): Boolean {

        val tempScore = this.score

        // Clean the transition record as we are doing a new move action.
        this.transitions = ArrayList()

        val changed = when (move) {
            GameMoves.Up -> actionMoveUp()
            GameMoves.Down -> actionMoveDown()
            GameMoves.Left -> actionMoveLeft()
            GameMoves.Right -> actionMoveRight()
        }

        // If board has changed then add new tile and store previous changes game board
        if (changed) {
            addNewTile()
            this.previousMoves.add(
                0,
                getGameBoardRecord()
            )  // index zero is current/last board result
            if (previousMoves.size > Constants.MAX_PREVIOUS_MOVES + 1) {
                this.previousMoves.removeAt(previousMoves.size - 1)
            }
            this.applyGameMoves()
        }

        if (this.score != tempScore) {
            delegate.userScoreChanged(score = this.score)
        }

        if (!hasMovesRemaining()) {
            this.gameOver = true
            if (this.maxTile >= Constants.WIN_TARGET) {
                delegate.userWin()
            } else {
                delegate.userFail()
            }
            if (this.score != tempScore && this.score > this.previousHighScore) {
                delegate.userPB(this.score)
            }
            return false
        }

        return changed
    }

    // Callback to the delegate to render game board changes
    @RequiresApi(Build.VERSION_CODES.M)
    private fun applyGameMoves() {
        for (trans in this.transitions) {
            delegate.updateTileValue(trans)
        }
    }

    // Tile movement instructions record for the game board renderer
    class Transition(
        var action: TileMoveType,
        var value: Int,
        var location: Int,
        var oldLocation: Int = -1
    ) : Serializable

    // Snapshot Object containing the status of previous game moves
    class GameBoardRecord(
        var gameOver: Boolean,
        var maxTile: Int
    ) : Serializable
}