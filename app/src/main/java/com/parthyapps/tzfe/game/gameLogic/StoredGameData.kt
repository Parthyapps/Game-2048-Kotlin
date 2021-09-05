package com.parthyapps.tzfe.game.gameLogic

import android.content.Context
import com.parthyapps.tzfe.game.R

class StoredGameData(context: Context) {

    private val preferenceKey = context.getString(R.string.game_data_preference_key)
    private val highScoreKey = context.getString(R.string.game_data_highest_score_int_key)
    private val sPref = context.getSharedPreferences(this.preferenceKey, Context.MODE_PRIVATE)

    // Push highScore data to user defaults
    fun putHighScore(newHS: Int) {
        val editor = sPref.edit()
        editor.putInt(this.highScoreKey, newHS)
        editor.apply()
    }

    // Get highScore data from user defaults
    fun getHighScore(): Int {
        return this.sPref.getInt(this.highScoreKey, -1)
    }

}