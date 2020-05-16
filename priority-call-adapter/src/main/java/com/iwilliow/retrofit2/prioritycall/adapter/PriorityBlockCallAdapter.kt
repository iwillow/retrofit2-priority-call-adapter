package com.iwilliow.retrofit2.prioritycall.adapter

import com.iwilliow.lib.common.priorityblockqueue.PriorityExecutorService
import com.iwilliow.retrofit2.prioritycall.call.PriorityBlockCall
import com.iwilliow.retrofit2.prioritycall.call.SimplePriorityBlockCall
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type
import java.util.concurrent.Executor

/**
 * @param <R>
 * @author dogedoge233@hotmail.com
</R> */
class PriorityBlockCallAdapter<R>(
    private val mResponseType: Type,
    private val mTaskExecutor: PriorityExecutorService,
    private val mCallbackExecutor: Executor?,
    private val mPriority: Int
) : CallAdapter<R?, PriorityBlockCall<R?>> {
    override fun responseType(): Type {
        return mResponseType
    }

    override fun adapt(call: Call<R?>): PriorityBlockCall<R?> {
        return SimplePriorityBlockCall(
            mTaskExecutor,
            mCallbackExecutor,
            call,
            mPriority
        )
    }

}