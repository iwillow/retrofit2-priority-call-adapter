package com.iwilliow.lib.common.priorityblockqueue

import java.util.concurrent.*

/**
 * @author dogedoge233@hotmail.com
 */
class PriorityThreadPoolExecutor : ThreadPoolExecutor, PriorityExecutorService {
    //mLowerPriorityFirst为true表示低优先级先执行
    private var mLowerPriorityFirst: Boolean

    constructor(
        lowerPriorityFirst: Boolean, corePoolSize: Int,
        maximumPoolSize: Int,
        keepAliveTime: Long,
        unit: TimeUnit?,
        workQueue: PriorityBlockingQueue<Runnable>
    ) : super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue) {
        mLowerPriorityFirst = lowerPriorityFirst
    }

    constructor(
        lowerPriorityFirst: Boolean,
        corePoolSize: Int,
        maximumPoolSize: Int,
        keepAliveTime: Long,
        unit: TimeUnit?,
        workQueue: PriorityBlockingQueue<Runnable>,
        threadFactory: ThreadFactory?
    ) : super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory) {
        mLowerPriorityFirst = lowerPriorityFirst
    }

    constructor(
        lowerPriorityFirst: Boolean,
        corePoolSize: Int,
        maximumPoolSize: Int,
        keepAliveTime: Long,
        unit: TimeUnit?,
        workQueue: PriorityBlockingQueue<Runnable>,
        handler: RejectedExecutionHandler?
    ) : super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler) {
        mLowerPriorityFirst = lowerPriorityFirst
    }

    constructor(
        lowerPriorityFirst: Boolean,
        corePoolSize: Int,
        maximumPoolSize: Int,
        keepAliveTime: Long,
        unit: TimeUnit?,
        workQueue: PriorityBlockingQueue<Runnable>,
        threadFactory: ThreadFactory?,
        handler: RejectedExecutionHandler?
    ) : super(
        corePoolSize,
        maximumPoolSize,
        keepAliveTime,
        unit,
        workQueue,
        threadFactory,
        handler
    ) {
        mLowerPriorityFirst = lowerPriorityFirst
    }

    override fun submit(task: Runnable): Future<*> {
        return submitRunnable(TaskPriority.PRIORITY_DEFAULT, null, task)
    }

    override fun <T> submit(task: Runnable, result: T): Future<T> {
        return submitRunnable(TaskPriority.PRIORITY_DEFAULT, result, task)
    }

    override fun <T> submit(task: Callable<T>): Future<T> {
        return submitCallable(TaskPriority.PRIORITY_DEFAULT, task)
    }

    override fun <T> submitCallable(task: Callable<T>): Future<T> {
        return submitCallable(TaskPriority.PRIORITY_DEFAULT, task)
    }

    override fun <T> submitCallable(priority: Int, task: Callable<T>): Future<T> {
        val future = newTaskForCallable(priority, task)
        execute(future)
        return future
    }

    override fun submitRunnable(task: Runnable): Future<*> {
        return submitRunnable(TaskPriority.PRIORITY_DEFAULT, task)
    }

    override fun submitRunnable(priority: Int, task: Runnable): Future<*> {
        return submitRunnable(priority, null, task)
    }

    override fun <T> submitRunnable(priority: Int, result: T?, task: Runnable): Future<T> {
        val future = newTaskForRunnable(priority, result, task)
        execute(future)
        return future
    }

    override fun executeRunnable(command: Runnable) {
        executeRunnable(TaskPriority.PRIORITY_DEFAULT, command)
    }

    override fun executeRunnable(priority: Int, command: Runnable) {
        super.execute(newTaskForRunnable<Any?>(priority, null, command))
    }

    override fun <T> newTaskForRunnable(runnable: Runnable): RunnableFuture<T> {
        return newTaskForRunnable(TaskPriority.PRIORITY_DEFAULT, runnable)
    }

    override fun <T> newTaskForRunnable(priority: Int, runnable: Runnable): RunnableFuture<T> {
        return newTaskForRunnable(priority, null, runnable)
    }

    override fun <T> newTaskForRunnable(
        priority: Int,
        value: T?,
        runnable: Runnable
    ): RunnableFuture<T> {
        return ComparableFutureTask(runnable, value, priority, mLowerPriorityFirst)
    }

    override fun <T> newTaskForCallable(callable: Callable<T>): RunnableFuture<T> {
        return newTaskForCallable(TaskPriority.PRIORITY_DEFAULT, callable)
    }

    override fun <T> newTaskForCallable(priority: Int, callable: Callable<T>): RunnableFuture<T> {
        return ComparableFutureTask(callable, priority, mLowerPriorityFirst)
    }

    override fun shutdownTasks() {
        shutdown()
    }

    override fun shutDownTasksNow(): List<Runnable>? {
        return shutdownNow()
    }
}