package com.onyx.client.core

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * Real-time FPS Display Overlay
 * Shows actual FPS regardless of VSync or screen refresh rate
 */
class FPSDisplay @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var fps = 0
    private var frameCount = 0L
    private var lastTime = System.currentTimeMillis()

    private val paint = Paint().apply {
        color = Color.GREEN
        textSize = 48f
        isAntiAlias = true
        setShadowLayer(4f, 2f, 2f, Color.BLACK)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Calculate FPS
        val currentTime = System.currentTimeMillis()
        frameCount++

        if (currentTime - lastTime >= 1000) {
            fps = frameCount.toInt()
            frameCount = 0
            lastTime = currentTime
        }

        // Color based on FPS
        paint.color = when {
            fps >= 150 -> Color.GREEN      // 150+ FPS
            fps >= 100 -> Color.YELLOW     // 100+ FPS
            fps >= 60 -> Color.CYAN        // 60+ FPS
            else -> Color.RED              // Below 60
        }

        // Draw FPS text
        canvas.drawText(
            "FPS: $fps",
            50f,
            100f,
            paint
        )

        // Draw status
        paint.color = Color.WHITE
        paint.textSize = 32f
        canvas.drawText(
            when {
                fps >= 200 -> "ULTRA 🔥"
                fps >= 150 -> "EXTREME ⚡"
                fps >= 100 -> "HIGH ✓"
                fps >= 60 -> "GOOD ✓"
                else -> "LOW ⚠"
            },
            50f,
            180f,
            paint
        )

        invalidate()
    }

    fun updateFps(newFps: Int) {
        fps = newFps
    }
}
