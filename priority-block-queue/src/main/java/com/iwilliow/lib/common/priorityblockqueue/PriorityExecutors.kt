package com.iwilliow.lib.common.priorityblockqueue

import java.util.concurrent.PriorityBlockingQueue
import java.util.concurrent.TimeUnit

/**
 * @author dogedoge233@hotmail.com
 */
object PriorityExecutors {

    @JvmOverloads
    @JvmStatic
    fun newFixedThreadPool(
        lowerPriorityFirst: Boolean = false,
        nThreads: Int
    ): PriorityExecutorService {
        return PriorityThreadPoolExecutor(
            lowerPriorityFirst,
            nThreads,
            nThreads,
            0L,
            TimeUnit.MILLISECONDS,
            PriorityBlockingQueue()
        )
    }

    @JvmOverloads
    @JvmStatic
    fun newSingleThreadExecutor(lowerPriorityFirst: Boolean = false): PriorityExecutorService {
        return PriorityThreadPoolExecutor(
            lowerPriorityFirst,
            1,
            1,
            0L,
            TimeUnit.MILLISECONDS,
            PriorityBlockingQueue(1024)
        )
    }
}