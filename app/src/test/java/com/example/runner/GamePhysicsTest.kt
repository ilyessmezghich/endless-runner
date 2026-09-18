package com.example.runner

import kotlin.math.min
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.junit.jupiter.api.Test

class GamePhysicsTest {

    @Test
    fun startsRestingOnTheGround() {
        val physics = GamePhysics(gravity = 1800f, jumpPower = -750f)
        physics.placeOnGround(200f)
        assertTrue(physics.onGround)
        assertEquals(200f, physics.y)
        assertEquals(0f, physics.velocity)
    }

    @Test
    fun jumpOnlyWorksFromTheGround() {
        val physics = GamePhysics(1800f, -750f)
        physics.placeOnGround(200f)
        physics.jump()
        assertFalse(physics.onGround)
        assertEquals(-750f, physics.velocity)

        physics.jump()
        assertEquals(-750f, physics.velocity, "mid-air jumps must be ignored")
    }

    @Test
    fun playerRisesThenReturnsToGround() {
        val physics = GamePhysics(1800f, -750f)
        physics.placeOnGround(200f)
        physics.jump()
        var minY = physics.y
        for (i in 0..299) {
            physics.update(1f / 60f)
            minY = min(minY, physics.y)
        }
        assertTrue(minY < 200f, "player should rise above the ground")
        assertTrue(physics.onGround, "player should land back on the ground")
        assertEquals(200f, physics.y, 0.001f)
    }

    @Test
    fun playerNeverSinksBelowTheGround() {
        val physics = GamePhysics(1800f, -750f)
        physics.placeOnGround(100f)
        physics.jump()
        for (i in 0..1000) {
            physics.update(1f / 30f)
            assertTrue(physics.y >= 100f - 0.001f)
        }
    }
}