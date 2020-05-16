package com.iwilliow.retrofit2.prioritycall.annotation

import com.iwilliow.lib.common.priorityblockqueue.TaskPriority


@MustBeDocumented
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class Priority(
    @Priorities val value: Int = TaskPriority.PRIORITY_DEFAULT,
    val description: String = ""
)