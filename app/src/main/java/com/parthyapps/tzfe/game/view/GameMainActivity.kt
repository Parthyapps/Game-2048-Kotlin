package com.parthyapps.tzfe.game.view

import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.parthyapps.tzfe.game.R
import com.parthyapps.tzfe.game.databinding.ActivityGameMainBinding
import com.parthyapps.tzfe.game.gameLogic.GameFunctionality
import com.parthyapps.tzfe.game.gameLogic.GameFunctionProtocol
import com.parthyapps.tzfe.game.gameLogic.GameMoves
import com.parthyapps.tzfe.game.gameLogic.StoredGameData
import com.parthyapps.tzfe.game.gameLogic.TileMoveType

class GameMainActivity : AppCompatActivity(), GameFunctionProtocol {

    private lateinit var activityGameMainBinding: ActivityGameMainBinding

    private val cells = IntArray(16)
    private var highScore: Int = 0
    private var x1 = 0f
    private var y1 = 0f
    private lateinit var game: StartNewGame
    private lateinit var dataStore: StoredGameData

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_main)
        activityGameMainBinding = ActivityGameMainBinding.inflate(layoutInflater)
        val view: View = activityGameMainBinding.root
        setContentView(view)
        dataStore = StoredGameData(this.baseContext)

        cells[0] = R.id.index0
        cells[1] = R.id.index1
        cells[2] = R.id.index2
        cells[3] = R.id.index3
        cells[4] = R.id.index4
        cells[5] = R.id.index5
        cells[6] = R.id.index6
        cells[7] = R.id.index7
        cells[8] = R.id.index8
        cells[9] = R.id.index9
        cells[10] = R.id.index10
        cells[11] = R.id.index11
        cells[12] = R.id.index12
        cells[13] = R.id.index13
        cells[14] = R.id.index14
        cells[15] = R.id.index15

        if (savedInstanceState != null) {
            val tmpGame: StartNewGame? =
                savedInstanceState.getSerializable(GAME_KEY) as StartNewGame?
            if (tmpGame != null) {
                game = tmpGame
                game.replotBoard()
            }
        } else {
            game = StartNewGame(this)
            this.setupNewGame()
        }

        activityGameMainBinding.resetButton.setOnClickListener {
            this.setupNewGame()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable(GAME_KEY, game)
        super.onSaveInstanceState(outState)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val tmpGame: StartNewGame? =
            savedInstanceState.getSerializable(GAME_KEY) as StartNewGame?
        if (tmpGame != null) {
            game = tmpGame
            game.replotBoard()
        }
    }


    // Start / Reset game here ->
    @RequiresApi(Build.VERSION_CODES.M)
    private fun setupNewGame() {
        this.highScore = dataStore.getHighScore()
        game.newGame(this.highScore)
        this.userScoreChanged(0)
    }

    // onTouchEvent () method gets called when User performs any touch event on screen
    // Method to handle touch event like left to right swap and right to left swap
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onTouchEvent(event: MotionEvent): Boolean {

        when (event.action) {
            // when user first touches the screen we get x and y coordinate
            MotionEvent.ACTION_DOWN -> {
                x1 = event.x
                y1 = event.y
            }
            MotionEvent.ACTION_UP -> {
                val x2 = event.x
                val y2 = event.y
                val minDistance = 200

                if (x1 < x2 && x2 - x1 > minDistance) {
                    game.actionMove(GameMoves.Right)
                } else if (x1 > x2 && x1 - x2 > minDistance) {
                    game.actionMove(GameMoves.Left)
                } else if (y1 < y2 && y2 - y1 > minDistance) {
                    game.actionMove(GameMoves.Down)
                } else if (y1 > y2 && y1 - y2 > minDistance) {
                    game.actionMove(GameMoves.Up)
                }
            }
        }
        return super.onTouchEvent(event)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun paintTransition(move: GameFunctionality.Transition) {

        val tv = findViewById<TextView>(cells[move.location])
        //val tv = cells[move.location]
        if (move.action == TileMoveType.Slide || move.action == TileMoveType.Merge) {
            paintCell(tv, move.value)
            this.paintTransition(
                GameFunctionality.Transition(
                    TileMoveType.Clear,
                    0,
                    move.oldLocation
                )
            )
        } else {
            paintCell(tv, move.value)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun paintCell(obj: Any, value: Int) {

        val tv = obj as TextView
        tv.text = if (value <= 0) "" else "$value"

        var txCol = resources.getColor(R.color.t_dark_text, null)
        var bgCol = resources.getColor(R.color.t0_bg, null)

        when (value) {
            2 -> bgCol = resources.getColor(R.color.t2_bg, null)
            4 -> bgCol = resources.getColor(R.color.t4_bg, null)
            8 -> {
                txCol = resources.getColor(R.color.t_light_text, null)
                bgCol = resources.getColor(R.color.t8_bg, null)
            }
            16 -> {
                txCol = resources.getColor(R.color.t_light_text, null)
                bgCol = resources.getColor(R.color.t16_bg, null)
            }
            32 -> {
                txCol = resources.getColor(R.color.t_light_text, null)
                bgCol = resources.getColor(R.color.t32_bg, null)
            }
            64 -> {
                txCol = resources.getColor(R.color.t_light_text, null)
                bgCol = resources.getColor(R.color.t64_bg, null)
            }
            128 -> {
                txCol = resources.getColor(R.color.t_light_text, null)
                bgCol = resources.getColor(R.color.t128_bg, null)
            }
            256 -> bgCol = resources.getColor(R.color.t256_bg, null)
            512 -> bgCol = resources.getColor(R.color.t512_bg, null)
            1024 -> bgCol = resources.getColor(R.color.t1024_bg, null)
            2048 -> bgCol = resources.getColor(R.color.t2048_bg, null)
            else -> {
            } // won't happen
        }
        tv.setBackgroundColor(bgCol)
        tv.setTextColor(txCol)
    }

    override fun userWin() {
        Toast.makeText(
            this, resources.getString(R.string.winner_toast_message) +
                    game.maxTile, Toast.LENGTH_LONG
        ).show()
    }

    override fun userFail() {
        Toast.makeText(
            this, resources.getString(R.string.lose_toast_message),
            Toast.LENGTH_LONG
        ).show()
    }

    override fun userPB(score: Int) {
        Toast.makeText(
            this, resources.getString(R.string.personalbest_toast_message),
            Toast.LENGTH_SHORT
        ).show()
        this.dataStore.putHighScore(score)
        this.highScore = score
    }

    override fun userScoreChanged(score: Int) {
        activityGameMainBinding.score.text =
            StringBuffer(resources.getString(R.string.score)).append(score)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun updateTileValue(move: GameFunctionality.Transition) {
        paintTransition(move)
    }

    companion object {
        private const val GAME_KEY = "2048_GAME_KEY"
    }

}