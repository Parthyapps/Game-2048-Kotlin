package com.parthyapps.tzfe.game.utils

class Constants {
    companion object {

        // Game specific values
        const val WIN_TARGET = 2048
        const val EMPTY_TILE_VAL = 0
        const val MAX_PREVIOUS_MOVES = 5

        // Board specifics
        const val DIMENSION = 4
        const val TILE_CNT = 16

        // Random tile selection ratio
        const val RANDOM_RATIO = 70  // favours '2's over '4's by ~3.5:1 ~70%

    }
}