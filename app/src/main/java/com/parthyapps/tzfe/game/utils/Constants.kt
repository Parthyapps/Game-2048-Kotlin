package com.parthyapps.tzfe.game.utils

import android.graphics.Color
import android.os.Build

class Constants {
    companion object {

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

        // Random tile selection ratio
        const val RANDOM_RATIO = 70  // favours '2's over '4's by ~3.5:1 ~70%

    }
}