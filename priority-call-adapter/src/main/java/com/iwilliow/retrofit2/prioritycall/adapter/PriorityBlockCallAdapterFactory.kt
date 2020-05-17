package com.iwilliow.retrofit2.prioritycall.adapter

import android.os.Handler
import android.os.Looper
import com.iwilliow.lib.common.priorityblockqueue.PriorityExecutorService
import com.iwilliow.lib.common.priorityblockqueue.PriorityExecutors.newFixedThreadPool
import com.iwilliow.lib.common.priorityblockqueue.PriorityExecutors.newSingleThreadExecutor
import com.iwilliow.lib.common.priorityblockqueue.TaskPriority
import com.iwilliow.retrofit2.prioritycall.annotation.Priority
import com.iwilliow.retrofit2.prioritycall.call.PriorityBlockCall
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.SkipCallbackExecutor
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.concurrent.Executor

/**
 * @author dogedoge233@hotmail.com
 */
class PriorityBlockCallAdapterFactory private constructor(
    private val mTaskExecutor: PriorityExecutorService,
    private val mCallbackExecutor: Executor
) : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != PriorityBlockCall::class.java) {
            return null
        }
        require(returnType is ParameterizedType) { "Call return type must be parameterized as PriorityBlockCall<Foo> or PriorityBlockCall<? extends Foo>" }
        val responseType = getParameterUpperBound(0, returnType)
        val callBackExecutor = if (isAnnotationPresent(
                annotations,
                SkipCallbackExecutor::class.java
            )
        ) null else mCallbackExecutor
        var priority = TaskPriority.PRIORITY_DEFAULT
        val annotation = getPresentAnnotation(annotations, Priority::class.java)
        if (annotation is Priority) {
            priority = annotation.value
        }
        return PriorityBlockCallAdapter<Any?>(
            responseType,
            mTaskExecutor,
            callBackExecutor,
            priority
        )
    }

    private class MainThreadExecutor : Executor {
        private val handler = Handler(Looper.getMainLooper())
        override fun execute(r: Runnable) {
            handler.post(r)
        }
    }

    companion object {
        @JvmStatic
        fun create(): PriorityBlockCallAdapterFactory {
            return PriorityBlockCallAdapterFactory(
                sTaskExecutor,
                sCallbackExecutor
            )
        }

        @JvmStatic
        fun create(nThread: Int): PriorityBlockCallAdapterFactory {
            return PriorityBlockCallAdapterFactory(
                newFixedThreadPool(false, nThread),
                sCallbackExecutor
            )
        }

        /**
         * single priority block call
         */
        @JvmStatic
        private val sTaskExecutor = newSingleThreadExecutor()
        /**
         * the callback's  Executor
         */
        @JvmStatic
        private val sCallbackExecutor: Executor = MainThreadExecutor()

        private fun isAnnotationPresent(
            annotations: Array<Annotation>,
            cls: Class<out Annotation>
        ): Boolean {
            for (annotation in annotations) {
                if (cls.isInstance(annotation)) {
                    return true
                }
            }
            return false
        }

        private fun getPresentAnnotation(
            annotations: Array<Annotation>,
            cls: Class<out Annotation>
        ): Annotation? {
            for (annotation in annotations) {
                if (cls.isInstance(annotation)) {
                    return annotation
                }
            }
            return null
        }
    }

}