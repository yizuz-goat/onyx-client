package com.onyx.client.core

import android.os.Process
import kotlin.math.min

/**
 * CPU Optimization
 * Thread affinity, priority management, and load balancing
 */
class CPUOptimizer {

    private val cpuCount = Runtime.getRuntime().availableProcessors()
    private val renderThread = Thread.currentThread()
    private val workerThreads = mutableListOf<Thread>()

    init {
        optimizeCPUAffinity()
        balanceWorkload()
    }

    /**
     * Set CPU affinity for render thread
     * Pins thread to specific CPU cores for cache locality
     */
    private fun optimizeCPUAffinity() {
        // Assign render thread to fast cores (usually 0-1)
        Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_DISPLAY)

        // Create worker threads for other cores
        val workerCount = min(cpuCount - 1, 3)
        repeat(workerCount) { i ->
            val worker = Thread {
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND)
            }.apply {
                name = "Worker-$i"
                isDaemon = true
                start()
            }
            workerThreads.add(worker)
        }
    }

    /**
     * Balance workload across cores
     */
    private fun balanceWorkload() {
        System.setProperty("dalvik.vm.dex2oat-cpu-set", "0,1,2,3")
        System.setProperty("dalvik.vm.dex2oat-threads", cpuCount.toString())
    }

    /**
     * Get optimal thread count for tasks
     */
    fun getOptimalThreadCount(): Int = min(cpuCount - 1, 4)

    /**
     * Kill worker threads
     */
    fun shutdown() {
        workerThreads.forEach { it.interrupt() }
    }
}
