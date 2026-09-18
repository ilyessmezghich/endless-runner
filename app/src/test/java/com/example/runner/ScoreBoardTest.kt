package com.example.runner

import kotlin.test.assertEquals
import org.junit.jupiter.api.Test

class ScoreBoardTest {

    @Test
    fun scoreAccumulatesOverTime() {
        val board = ScoreBoard()
        board.add(0.5f, 10f)
        board.add(0.5f, 10f)
        assertEquals(10, board.currentScore())
    }

    @Test
    fun highScorePersistsAcrossRuns() {
        val board = ScoreBoard()
        board.add(30f, 10f)
        board.endRun()
        assertEquals(300, board.highScore)

        board.reset()
        board.add(10f, 10f)
        board.endRun()
        assertEquals(300, board.highScore, "high score must not decrease")
    }

    @Test
    fun betterRunUpdatesTheHighScore() {
        val board = ScoreBoard()
        board.add(10f, 10f)
        board.endRun()
        assertEquals(100, board.highScore)

        board.reset()
        board.add(50f, 10f)
        board.endRun()
        assertEquals(500, board.highScore)
    }

    @Test
    fun resetClearsOnlyTheCurrentScore() {
        val board = ScoreBoard()
        board.add(20f, 10f)
        board.reset()
        assertEquals(0, board.currentScore())
        assertEquals(0, board.highScore)
    }
}