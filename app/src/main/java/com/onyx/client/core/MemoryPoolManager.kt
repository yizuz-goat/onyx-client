package com.onyx.client.core

import android.app.ActivityManager
import android.content.Context
import java.util.concurrent.ConcurrentHashMap

/**
 * Advanced Memory Management
 * Object pooling, allocation pooling, and garbage collection optimization
 */
class MemoryPoolManager(private val context: Context) {

    private val pools = ConcurrentHashMap<Class<*>, ArrayDeque<Any>>()
    private val allocationStats = ConcurrentHashMap<String, Long>()
    private val maxPoolSize = 1000

    /**
     * Get or allocate object from pool
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> acquire(clazz: Class<T>): T {
        val pool = pools.getOrPut(clazz) { ArrayDeque(maxPoolSize) }
        return if (pool.isNotEmpty()) {
            pool.removeFirst() as T
        } else {
            clazz.getDeclaredConstructor().newInstance()
        }
    }

    /**
     * Return object to pool for reuse
     */
    fun <T> release(obj: T) {
        val clazz = obj!!::class.java
        val pool = pools.getOrPut(clazz) { ArrayDeque(maxPoolSize) }
        if (pool.size < maxPoolSize) {
            pool.addLast(obj)
        }
    }

    /**
     * Get memory info
     */
    fun getMemoryInfo(): MemoryInfo {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runtime = Runtime.getRuntime()

        return MemoryInfo(
            totalMemory = runtime.totalMemory(),
            freeMemory = runtime.freeMemory(),
            maxMemory = runtime.maxMemory(),
            usedMemory = runtime.totalMemory() - runtime.freeMemory(),
            nativeHeap = activityManager.memoryClass.toLong(),
            poolSize = pools.values.sumOf { it.size }.toLong()
        )
    }

    /**
     * Force garbage collection (use sparingly)
     */
    fun forceGarbageCollection() {
        System.gc()
    }

    /**
     * Track allocations for profiling
     */
    fun trackAllocation(tag: String) {
        allocationStats[tag] = (allocationStats[tag] ?: 0) + 1
    }

    data class MemoryInfo(
        val totalMemory: Long,
        val freeMemory: Long,
        val maxMemory: Long,
        val usedMemory: Long,
        val nativeHeap: Long,
        val poolSize: Long
    )
}
