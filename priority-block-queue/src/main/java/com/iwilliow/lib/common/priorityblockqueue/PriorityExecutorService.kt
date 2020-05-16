package com.iwilliow.lib.common.priorityblockqueue

import java.util.concurrent.Callable
import java.util.concurrent.Future
import java.util.concurrent.RunnableFuture

/**
 * @author dogedoge233@hotmail.com
 */
interface PriorityExecutorService {

    fun <T> submitCallable(task: Callable<T>): Future<T>

    fun <T> submitCallable(priority: Int, task: Callable<T>): Future<T>

    fun submitRunnable(task: Runnable): Future<*>

    fun submitRunnable(priority: Int, task: Runnable): Future<*>

    fun <T> submitRunnable(priority: Int, result: T?, task: Runnable): Future<T>

    fun executeRunnable(command: Runnable)

    fun executeRunnable(priority: Int, command: Runnable)

    fun <T> newTaskForRunnable(runnable: Runnable): RunnableFuture<T>

    fun <T> newTaskForRunnable(priority: Int, runnable: Runnable): RunnableFuture<T>

    fun <T> newTaskForRunnable(priority: Int, value: T?, runnable: Runnable): RunnableFuture<T>

    fun <T> newTaskForCallable(callable: Callable<T>): RunnableFuture<T>

    fun <T> newTaskForCallable(priority: Int, callable: Callable<T>): RunnableFuture<T>

    fun shutdownTasks()

    fun shutDownTasksNow(): List<Runnable>?
}