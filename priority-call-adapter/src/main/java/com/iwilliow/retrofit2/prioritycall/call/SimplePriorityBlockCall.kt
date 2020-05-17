package com.iwilliow.retrofit2.prioritycall.call

import android.util.Log
import com.iwilliow.lib.common.priorityblockqueue.PriorityExecutorService
import com.iwilliow.retrofit2.prioritycall.BuildConfig
import com.iwilliow.retrofit2.prioritycall.annotation.Priorities
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.concurrent.Callable
import java.util.concurrent.Executor
import java.util.concurrent.Future

/**
 * @param <T>
 * @author dogedoge233@hotmail.com
 */
class SimplePriorityBlockCall<T>(
    private val mPriorityTaskExecutor: PriorityExecutorService,
    private val mCallbackExecutor: Executor?,
    override val delegateCall: Call<T>,
    private val mPriority: Int
) : PriorityBlockCall<T> {
    @Volatile
    private var mFuture: Future<ResponseWrapper<T>>? = null
    @Volatile
    private var mCanceled = false
    @Volatile
    private var mExecuted = false

    @Throws(Throwable::class)
    override fun executeBlockCall(): Response<T>? {
        return executeBlockCall(mPriority)
    }

    @Throws(Throwable::class)
    override fun executeBlockCall(@Priorities priority: Int): Response<T>? {
        if (mExecuted) throw IllegalStateException("Already executed.")
        else {
            mExecuted = true
            if (mFuture == null) {
                synchronized(this) {
                    if (mFuture == null) {
                        mFuture = mPriorityTaskExecutor.submitCallable(
                            priority,
                            Callable { processCall(null, false, priority) }
                        )
                    }
                }
            }
            val wrapper = mFuture!!.get()
            return wrapper.response ?: throw wrapper.throwable!!
        }

    }

    override fun enqueueBlockCall(callback: Callback<T>?) {
        return enqueueBlockCall(mPriority, callback)
    }

    override fun enqueueBlockCall(@Priorities priority: Int, callback: Callback<T>?) {
        if (mExecuted) {
            failedCallback(delegateCall, IllegalStateException("Already executed."), callback)
        } else {
            mExecuted = true
            mPriorityTaskExecutor.executeRunnable(
                priority,
                Runnable { processCall(callback, true, priority) })
        }

    }

    private fun processCall(
        asyncCallback: Callback<T>?,
        async: Boolean,
        priority: Int
    ): ResponseWrapper<T> {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "start processing priority:$priority task")
        }
        val call: Call<T> = if (async) { //async call
            cloneBlockCall().delegateCall
        } else { //sync call
            delegateCall.clone()
        }
        return (try {
            val response = call.execute()
            successCallback(call, response, asyncCallback)
            ResponseWrapper(response, null)
        } catch (e: IOException) {
            failedCallback(call, e, asyncCallback)
            ResponseWrapper<T>(null, e)
        }).also {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "finish processing priority:$priority task")
            }
        }
    }

    private fun successCallback(call: Call<T>, response: Response<T>, callback: Callback<T>?) {
        mCallbackExecutor?.execute {
            if (mCanceled || call.isCanceled) {
                // Emulate OkHttp's behavior of throwing/delivering an IOException on cancellation.
                failedCallback(call, IOException("Canceled"), callback)
            } else {
                callback?.onResponse(call, response)
            }
        } ?: callback?.onResponse(call, response)
    }

    private fun failedCallback(call: Call<T>, t: Throwable, callback: Callback<T>?) {
        mCallbackExecutor?.execute {
            callback?.onFailure(call, t)
        } ?: callback?.onFailure(call, t)
    }

    override val isBlockCallExecuted: Boolean
        get() = delegateCall.isExecuted || mExecuted

    override fun cancelBlockCall() {
        mCanceled = true
        delegateCall.cancel()
    }

    override val isBlockCallCanceled: Boolean
        get() = mCanceled || delegateCall.isCanceled

    override fun cloneBlockCall(): PriorityBlockCall<T> {
        return SimplePriorityBlockCall<T>(
            mPriorityTaskExecutor,
            mCallbackExecutor,
            delegateCall.clone(),
            mPriority
        )
    }

    override val blockCallRequest: Request
        get() = delegateCall.request()

    private class ResponseWrapper<T> internal constructor(
        val response: Response<T>?,
        val throwable: Throwable?
    )

    private companion object {
        const val TAG = "SimplePriorityBlockCall"
    }
}