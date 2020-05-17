package com.iwilliow.app.prioritycall.adapter.retrofit

import com.iwilliow.retrofit2.prioritycall.adapter.PriorityBlockCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class RetrofitManager private constructor() {
    val mRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addCallAdapterFactory(PriorityBlockCallAdapterFactory.create())
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