package com.parthyapps.tzfe.game.utils

import android.graphics.Color
import android.os.Build

class Constants {
    companion object {
        // STD message strings EN
        const val NO_MORE_MOVES = "Sorry. No more moves possible. Try Undo?"
        const val NO_MORE_UNDO = "Sorry. No undo's available. Undo's are limited to 5!"
        const val NEW_HIGH_SCORE = "Oh YES. New PB reached."
        const val WINNER = "AWESOME!, you have won the game. Keep playing!!!"

        // Game specific values
        const val WIN_TARGET = 2048
        const val EMPTY_TILE_VAL = 0
        const val MAX_PREVIOUS_MOVES = 5
        const val PB_MESG_THRESHOLD = 100

        // Board specifics
        const val DIMENSION = 4
        const val TILE_CNT = 16
        const val TILE_WIDTH = 65.5
        const val TILE_PADDING = 8.0
        const val BOARD_CORNER_RADIUS = 2.0
        const val TILE_CORNER_RADIUS = 5

        // Animation duration in seconds
        const val ZERO_DURATION = 0.00
        const val QUICK_DURATION = 0.09
        const val NORMAL_DURATION = 0.25
        const val SLOW_DURATION = 0.45
        const val LONG_DURATION = 0.60
        const val PAUSE_DURATION = 1.00

        // Fonts for various occasions
        ///val SMALL_NUMBER_FONTS : ()->(name:String, size:Int) : ()-> { return (name:"HelveticaNeue-Bold", size:38) }
        //val MEDIUM_NUMBER_FONTS = (name: "HelveticaNeue-Bold", size: 36)
        //val LARGE_NUMBER_FONTS = (name: "HelveticaNeue-Bold", size: 28)

        // Random tile selection ratio
        const val RANDOM_RATIO = 70  // favours '2's over '4's by ~3.5:1 ~70%

        // Images
        //val SOUND_ON_IMG = #imageLiteral(resourceName: "sound_on")
        //val SOUND_OFF_IMG = #imageLiteral(resourceName: "sound_off")

    }
}