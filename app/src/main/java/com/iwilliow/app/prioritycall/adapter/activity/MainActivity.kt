package com.iwilliow.app.prioritycall.adapter.activity

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.iwilliow.app.prioritycall.adapter.R
import com.iwilliow.app.prioritycall.adapter.api.SampleApi
import com.iwilliow.app.prioritycall.adapter.retrofit.RetrofitManager
import com.iwilliow.lib.common.priorityblockqueue.TaskPriority
import com.iwilliow.retrofit2.prioritycall.call.enqueueBlockCall
import com.iwilliow.retrofit2.prioritycall.call.onFailure
import com.iwilliow.retrofit2.prioritycall.call.onResponse
import retrofit2.Call
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var mTvResponse: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mTvResponse = findViewById(R.id.tv_response)
        findViewById<View>(R.id.btn_request).setOnClickListener {
            request()
        }
    }

    private fun request() {
        RetrofitManager.getRetrofit()
            .create(SampleApi::class.java)
            .contributors("square", "retrofit")
            .enqueueBlockCall(
                priority = TaskPriority.PRIORITY_HIGH,
                onResponse = { call: Call<String?>?, response: Response<String?>? ->
                    mTvResponse.append(response?.body())
                },
                onFailure = { call: Call<String?>?, t: Throwable ->

                }
            )

        RetrofitManager.getRetrofit()
            .create(SampleApi::class.java)
            .contributors("square", "retrofit")
            .onResponse { call, response ->
                mTvResponse.append(response?.body())
            }


        RetrofitManager.getRetrofit()
            .create(SampleApi::class.java)
            .contributors("square", "retrofit")
            .onFailure { call, t ->

            }
    }
}



