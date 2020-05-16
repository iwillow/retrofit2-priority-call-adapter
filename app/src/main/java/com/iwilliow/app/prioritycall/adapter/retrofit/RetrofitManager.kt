package com.iwilliow.app.prioritycall.adapter.retrofit

import retrofit2.Retrofit

class RetrofitManager private constructor() {
    val mRetrofit: Retrofit by lazy {
        Retrofit.Builder()
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