package com.example.runner

import kotlin.math.max

class ScoreBoard {

    var score: Float = 0f
    var highScore: Int = 0

    fun add(dt: Float, rate: Float) {
        score += dt * rate
    }

    fun currentScore(): Int = score.toInt()

    fun endRun() {
        highScore = max(highScore, score.toInt())
    }

    fun reset() {
        score = 0f
    }
}