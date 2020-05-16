package com.iwilliow.retrofit2.prioritycall.call

import com.iwilliow.lib.common.priorityblockqueue.TaskPriority
import com.iwilliow.retrofit2.prioritycall.annotation.Priorities
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @author dogedoge233@hotmail.com
 * @param <T>
 */
interface PriorityBlockCall<T> {
    /**
     * Synchronously send the request and return its response.
     *
     * @return
     * @throws Throwable
     */
    @Throws(Throwable::class)
    fun executeBlockCall(): Response<T>?

    /**
     * Synchronously send the request and return its response.
     *
     * @param priority
     * @return
     * @throws Throwable
     */
    @Throws(Throwable::class)
    fun executeBlockCall(@Priorities priority: Int): Response<T>?

    fun enqueueBlockCall(callback: Callback<T>?): PriorityBlockCall<T>
    /**
     * Asynchronously send the request and notify `callback` of its response or if an error
     * occurred talking to the server, creating the request, or processing the response.
     *
     * @param priority
     * @param callback
     */
    fun enqueueBlockCall(@Priorities priority: Int, callback: Callback<T>?): PriorityBlockCall<T>

    /**
     * Returns true if this call has been either [executed][.executeBlockCall] or [ ][.enqueueBlockCall]. It is an error to execute or enqueue a call more than once.
     */
    val isBlockCallExecuted: Boolean

    /**
     * Cancel this call. An attempt will be made to cancel in-flight calls, and if the call has not
     * yet been executed it never will be.
     */
    fun cancelBlockCall()

    /**
     * True if [.cancelBlockCall] was called.
     */
    val isBlockCallCanceled: Boolean

    /**
     * Create a new, identical call to this one which can be enqueued or executed even if this call
     * has already been.
     */
    fun cloneBlockCall(): PriorityBlockCall<T>

    /**
     * Get the original call
     *
     * @return
     */
    val delegateCall: Call<T>

    /**
     * The original HTTP request.
     */
    val blockCallRequest: Request?
}


inline fun <T> PriorityBlockCall<T>.onResponse(
    crossinline action: (
        call: Call<T>?,
        response: Response<T?>?
    ) -> Unit
) = enqueueBlockCall(onResponse = action)

inline fun <T> PriorityBlockCall<T>.onFailure(
    crossinline action: (
        call: Call<T>?,
        t: Throwable
    ) -> Unit
) = enqueueBlockCall(onFailure = action)

inline fun <T> PriorityBlockCall<T>.enqueueBlockCall(
    @Priorities priority: Int = TaskPriority.PRIORITY_DEFAULT,
    crossinline onResponse: (
        call: Call<T>?,
        response: Response<T?>
    ) -> Unit = { _, _ -> },
    crossinline onFailure: (
        call: Call<T>?,
        t: Throwable
    ) -> Unit = { _, _ -> }
) {
    enqueueBlockCall(priority,
        object : Callback<T> {

            override fun onResponse(call: Call<T>?, response: Response<T?>) {
                onResponse.invoke(call, response)
            }

            override fun onFailure(call: Call<T>?, t: Throwable) {
                onFailure.invoke(call, t)
            }
        })
}