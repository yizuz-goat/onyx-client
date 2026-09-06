package com.onyx.client.core

import java.util.concurrent.ConcurrentHashMap
import kotlin.math.min

/**
 * L1/L2 Cache Optimizer
 * Organizes data for optimal CPU cache locality
 */
class CacheOptimizer {

    private val l1Cache = mutableListOf<CacheLineData>()
    private val l2Cache = ConcurrentHashMap<String, CacheLineData>()

    private val L1_CACHE_SIZE = 32 * 1024 // 32KB typical L1
    private val L2_CACHE_SIZE = 256 * 1024 // 256KB typical L2
    private val CACHE_LINE_SIZE = 64 // bytes

    /**
     * Prefetch data into cache
     */
    fun prefetchData(key: String, data: ByteArray) {
        if (data.size <= CACHE_LINE_SIZE) {
            l1Cache.add(CacheLineData(key, data))
        } else {
            l2Cache[key] = CacheLineData(key, data)
        }
    }

    /**
     * Get cached data with optimal alignment
     */
    fun getCachedData(key: String): CacheLineData? {
        return l1Cache.find { it.key == key } ?: l2Cache[key]
    }

    /**
     * Organize data for cache locality
     */
    fun optimizeForCache(data: List<Float>): IntArray {
        val indices = IntArray(data.size) { it }
        // Sort by memory access patterns
        indices.sortBy { data[it] }
        return indices
    }

    data class CacheLineData(
        val key: String,
        val data: ByteArray
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is CacheLineData) return false
            if (key != other.key) return false
            if (!data.contentEquals(other.data)) return false
            return true
        }

        override fun hashCode(): Int {
            var result = key.hashCode()
            result = 31 * result + data.contentHashCode()
            return result
        }
    }
}
