package com.iwilliow.retrofit2.prioritycall.annotation

import androidx.annotation.IntDef
import com.iwilliow.lib.common.priorityblockqueue.TaskPriority

@IntDef(
    TaskPriority.PRIORITY_LOW,
    TaskPriority.PRIORITY_DEFAULT,
    TaskPriority.PRIORITY_MIDDLE,
    TaskPriority.PRIORITY_HIGH,
    TaskPriority.PRIORITY_VERY_HIGH
)
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
annotation class Priorities