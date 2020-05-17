package com.iwilliow.app.prioritycall.adapter.retrofit

import com.iwilliow.retrofit2.prioritycall.adapter.PriorityBlockCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class RetrofitManager private constructor() {
    val mRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            // can only run single one task at the same time order by priority
            .addCallAdapterFactory(PriorityBlockCallAdapterFactory.create())
             // can only run single 5 tasks at the same time  order by priority
            .addCallAdapterFactory(PriorityBlockCallAdapterFactory.create(5))
            .build()
    }

    companion object {

        private val instance: RetrofitManager by lazy {
            RetrofitManager()
        }

        @JvmStatic
        fun getRetrofit(): Retrofit {
            return instance.mRetrofit
        }

    }

}