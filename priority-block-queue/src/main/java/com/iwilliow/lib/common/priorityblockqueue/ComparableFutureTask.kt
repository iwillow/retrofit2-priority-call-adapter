package com.iwilliow.lib.common.priorityblockqueue

import java.util.concurrent.Callable
import java.util.concurrent.FutureTask

/**
 * @author dogedoge233@hotmail.com
 */
class ComparableFutureTask<V> : FutureTask<V>, Comparable<ComparableFutureTask<V>> {
    private var priority: Int
    /**
     * mLowerPriorityFirst为true表示低优先级先执行
     */
    private var mLowerPriorityFirst: Boolean

    @JvmOverloads
    constructor(
        callable: Callable<V>,
        priority: Int,
        lowerPriorityFirst: Boolean = false
    ) : super(
        callable
    ) {
        this.priority = priority
        mLowerPriorityFirst = lowerPriorityFirst
    }

    @JvmOverloads
    constructor(
        runnable: Runnable,
        result: V?,
        priority: Int,
        lowerPriorityFirst: Boolean = false
    ) : super(runnable, result) {
        this.priority = priority
        mLowerPriorityFirst = lowerPriorityFirst
    }

    override fun compareTo(o: ComparableFutureTask<V>): Int {
        return if (mLowerPriorityFirst) priority - o.priority else o.priority - priority
    }
}