package com.iwilliow.app.prioritycall.adapter.api

import com.iwilliow.lib.common.priorityblockqueue.TaskPriority
import com.iwilliow.retrofit2.prioritycall.annotation.Priority
import com.iwilliow.retrofit2.prioritycall.call.PriorityBlockCall
import retrofit2.http.GET
import retrofit2.http.Path

interface SampleApi {


    @Priority(
        value = TaskPriority.PRIORITY_MIDDLE,
        description = "getContributors"
    )
    @GET("repos/{owner}/{repo}/contributors")
    fun contributors(
        @Path("owner") owner: String?,
        @Path("repo") repo: String?
    ): PriorityBlockCall<String?>


    @Priority(
        value = TaskPriority.PRIORITY_VERY_HIGH,
        description = "getTop250"
    )
    @GET("https://douban.uieee.com/v2/movie/top250")
    fun getTop250(): PriorityBlockCall<String?>


    @Priority(
        value = TaskPriority.PRIORITY_LOW,
        description = "getMovie"
    )
    @GET("https://douban.uieee.com/v2/movie/subject/{subjectId}")
    fun getMovie(
        @Path("subjectId") subjectId: String
    ): PriorityBlockCall<String?>


}