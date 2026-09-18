package com.example.runner

import android.app.Activity
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView

class MainActivity : Activity() {

    private lateinit var gameView: GameView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gameView = GameView(this)
        setContentView(gameView)
    }

    override fun onResume() {
        super.onResume()
        gameView.resume()
    }

    override fun onPause() {
        super.onPause()
        gameView.pause()
    }
}

class GameView(context: android.content.Context) : SurfaceView(context), SurfaceHolder.Callback, Runnable {

    private val holder: SurfaceHolder = getHolder()
    private lateinit var thread: Thread
    private var running = false
    private var playing = false

    private val playerPaint = Paint().apply { color = Color.rgb(66, 165, 245) }
    private val groundPaint = Paint().apply { color = Color.rgb(120, 120, 120) }
    private val skyPaint = Paint().apply { color = Color.rgb(135, 206, 250) }
    private val obstaclePaint = Paint().apply { color = Color.rgb(239, 83, 80) }
    private val textPaint = Paint().apply {
        color = Color.WHITE
        textSize = 60f
        isAntiAlias = true
    }
    private val bigTextPaint = Paint().apply {
        color = Color.WHITE
        textSize = 72f
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
    }

    private var screenW = 0
    private var screenH = 0

    private val physics = GamePhysics(gravity = 1800f, jumpPower = -750f)
    private val scoreBoard = ScoreBoard()

    private val groundH = 160f
    private val playerSize = 80f
    private val runSpeed = 420f

    private val obstacles = ArrayList<RectF>()
    private var obstacleTimer = 0f
    private val obstacleGap = 0.9f
    private val obstacleW = 60f
    private val obstacleH = 130f

    private var started = false
    private var gameOver = false

    private var lastTime = 0L

    init {
        holder.addCallback(this)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        thread = Thread(this)
        running = true
        thread.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        screenW = width
        screenH = height
        reset()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        running = false
        try {
            thread.join()
        } catch (_: InterruptedException) {
        }
    }

    private fun reset() {
        physics.placeOnGround((screenH - groundH - playerSize).toFloat())
        scoreBoard.reset()
        obstacles.clear()
        obstacleTimer = 0f
        gameOver = false
        started = false
    }

    override fun run() {
        lastTime = System.nanoTime()
        while (running) {
            val now = System.nanoTime()
            val dt = ((now - lastTime) / 1_000_000_000f).coerceAtMost(0.05f)
            lastTime = now
            if (playing) {
                update(dt)
            }
            draw()
        }
    }

    private fun update(dt: Float) {
        if (gameOver) return
        if (!started) return

        scoreBoard.add(dt, 10f)
        physics.update(dt)

        obstacleTimer -= dt
        if (obstacleTimer <= 0f) {
            obstacles.add(RectF(screenW.toFloat(), screenH - groundH - obstacleH, screenW + obstacleW, screenH - groundH))
            obstacleTimer = obstacleGap * (0.7f + Math.random().toFloat() * 0.6f)
        }

        val playerLeft = screenW * 0.25f
        val playerTop = physics.y
        val playerRect = RectF(playerLeft, playerTop, playerLeft + playerSize, playerTop + playerSize)

        val iter = obstacles.iterator()
        while (iter.hasNext()) {
            val o = iter.next()
            o.left -= runSpeed * dt
            o.right -= runSpeed * dt
            if (o.right < 0) {
                iter.remove()
                continue
            }
            if (RectF.intersects(playerRect, o)) {
                gameOver = true
                scoreBoard.endRun()
            }
        }
    }

    private fun draw() {
        val canvas: Canvas? = holder.lockCanvas()
        canvas ?: return
        try {
            canvas.drawColor(Color.rgb(135, 206, 250))

            // ground
            canvas.drawRect(0f, screenH - groundH, screenW.toFloat(), screenH.toFloat(), groundPaint)

            // obstacles
            for (o in obstacles) {
                canvas.drawRect(o, obstaclePaint)
            }

            // player
            canvas.drawRect(screenW * 0.25f, physics.y, screenW * 0.25f + playerSize, physics.y + playerSize, playerPaint)

            // HUD
            if (started && !gameOver) {
                canvas.drawText("Score: ${scoreBoard.currentScore()}", 30f, 80f, textPaint)
            }

            if (!started) {
                canvas.drawText("TAP TO START", screenW / 2f, screenH / 2f - 40f, bigTextPaint)
                canvas.drawText("Best: ${scoreBoard.highScore}", screenW / 2f, screenH / 2f + 30f, textPaint)
            } else if (gameOver) {
                canvas.drawText("GAME OVER", screenW / 2f, screenH / 2f - 60f, bigTextPaint)
                canvas.drawText("Score: ${scoreBoard.currentScore()}   Best: ${scoreBoard.highScore}", screenW / 2f, screenH / 2f + 10f, textPaint)
                canvas.drawText("TAP TO RESTART", screenW / 2f, screenH / 2f + 80f, textPaint)
            }
        } finally {
            holder.unlockCanvasAndPost(canvas)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            if (gameOver) {
                reset()
                started = true
            } else if (!started) {
                started = true
                jump()
            } else {
                jump()
            }
            return true
        }
        return super.onTouchEvent(event)
    }

    private fun jump() {
        physics.jump()
    }

    fun resume() {
        playing = true
    }

    fun pause() {
        playing = false
    }
}