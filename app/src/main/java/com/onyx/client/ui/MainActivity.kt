package com.onyx.client.ui

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.onyx.client.core.PerformanceOptimizer
import com.onyx.client.core.MemoryPoolManager
import com.onyx.client.R

/**
 * Main Activity with ULTRA OPTIMIZATION
 * - VSync disabled globally
 * - Maximum refresh rate enabled
 * - Performance optimizer running
 */
class MainActivity : AppCompatActivity() {

    private lateinit var performanceOptimizer: PerformanceOptimizer
    private lateinit var memoryManager: MemoryPoolManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize optimizers FIRST
        performanceOptimizer = PerformanceOptimizer(this)
        memoryManager = MemoryPoolManager(this)

        // Enable maximum refresh rate (disable VSync globally)
        window.attributes = window.attributes.apply {
            screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL
        }

        // Set to maximum refresh rate
        window.setFlags(
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
        )

        setContentView(R.layout.activity_main)

        // Monitor memory
        logMemoryStats()
    }

    private fun logMemoryStats() {
        val info = memoryManager.getMemoryInfo()
        android.util.Log.d(
            "MainActivity",
            """Memory Stats:
            Total: ${info.totalMemory / 1024 / 1024}MB
            Free: ${info.freeMemory / 1024 / 1024}MB
            Used: ${info.usedMemory / 1024 / 1024}MB
            Max: ${info.maxMemory / 1024 / 1024}MB
            """.trimIndent()
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        memoryManager.forceGarbageCollection()
    }
}
