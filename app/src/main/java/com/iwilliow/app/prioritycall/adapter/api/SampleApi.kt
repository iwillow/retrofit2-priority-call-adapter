package com.iwilliow.app.prioritycall.adapter.api

import com.iwilliow.lib.common.priorityblockqueue.TaskPriority
import com.iwilliow.retrofit2.prioritycall.annotation.Priority
import com.iwilliow.retrofit2.prioritycall.call.PriorityBlockCall
import retrofit2.http.GET
import retrofit2.http.Path

interface SampleApi {


    @GET("https://api.github.com/repos/{owner}/{repo}/contributors")
    @Priority(
        value = TaskPriority.PRIORITY_MIDDLE,
        description = "getContributors"
    )
    fun contributors(
        @Path("owner") owner: String?,
        @Path("repo") repo: String?
    ): PriorityBlockCall<String?>
}