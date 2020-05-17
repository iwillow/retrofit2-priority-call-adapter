package com.iwilliow.app.prioritycall.adapter.activity

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.iwilliow.app.prioritycall.adapter.R
import com.iwilliow.app.prioritycall.adapter.api.SampleApi
import com.iwilliow.app.prioritycall.adapter.retrofit.RetrofitManager
import com.iwilliow.lib.common.priorityblockqueue.TaskPriority
import com.iwilliow.retrofit2.prioritycall.call.enqueueBlockCall
import com.iwilliow.retrofit2.prioritycall.call.onResponse
import retrofit2.Call
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private val tag = "MainActivity"
    private lateinit var mTvResponse: TextView
    private lateinit var mDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDialog = ProgressDialog(this)
        setContentView(R.layout.activity_main)
        mTvResponse = findViewById(R.id.tv_response)
        findViewById<View>(R.id.btn_request).setOnClickListener {
            request()
        }
    }

    private fun request() {
        mTvResponse.text = ""
        mDialog.show()
        RetrofitManager.getRetrofit()
            .create(SampleApi::class.java)
            .getMovie("25924056")
            .enqueueBlockCall(
                priority = TaskPriority.PRIORITY_HIGH,
                onResponse = { call: Call<String?>?, response: Response<String?>? ->
                    mTvResponse.append(response?.body())
                    mDialog.dismiss()
                },
                onFailure = { call: Call<String?>?, t: Throwable ->
                    mTvResponse.append(t.message)
                    mDialog.dismiss()
                }
            )

        RetrofitManager.getRetrofit()
            .create(SampleApi::class.java)
            .contributors("square", "retrofit")
            .onResponse { call, response ->
                Log.d(tag, response?.body())
            }
        RetrofitManager.getRetrofit()
            .create(SampleApi::class.java)
            .getTop250()
            .onResponse { call, response ->
                Log.d(tag, "PRIORITY_LOW:" + response?.body())
            }
    }

    override fun onStop() {
        super.onStop()
        mDialog.dismiss()
    }
}



