package com.example.runner

class GamePhysics(private val gravity: Float, private val jumpPower: Float) {

    var y: Float = 0f
    var velocity: Float = 0f
    var onGround: Boolean = true
    var groundY: Float = 0f

    fun placeOnGround(groundY: Float) {
        this.groundY = groundY
        y = groundY
        velocity = 0f
        onGround = true
    }

    fun jump() {
        if (onGround) {
            velocity = jumpPower
            onGround = false
        }
    }

    fun update(dt: Float) {
        if (onGround) return
        velocity += gravity * dt
        y += velocity * dt
        if (y >= groundY) {
            y = groundY
            velocity = 0f
            onGround = true
        }
    }
}