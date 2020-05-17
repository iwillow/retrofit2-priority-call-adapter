package com.iwilliow.app.prioritycall.adapter.activity

import android.Manifest.permission.INTERNET
import android.app.ProgressDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
            checkPermission()
        }
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                INTERNET
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(INTERNET), 123)
        } else {
            request()
        }
    }

    private fun request() {
        Log.d(tag, "request start")
        mTvResponse.text = ""
        mDialog.show()
        RetrofitManager.getRetrofit()
            .create(SampleApi::class.java)
            .getTop250()
            .enqueueBlockCall(
                onResponse = { call: Call<String?>?, response: Response<String?> ->
                    Log.d(tag, "PRIORITY_VERY_HIGH:\n" + response.body())
                    mDialog.dismiss()
                    mTvResponse.text = response?.body()

                },
                onFailure = { call: Call<String?>?, t: Throwable ->

                    Log.d(tag, "onFailure:" + t.message)
                    mDialog.dismiss()
                    mTvResponse.text = t.message
                }
            )
        RetrofitManager.getRetrofit()
            .create(SampleApi::class.java)
            .getMovie("30378158")
            .onResponse { call, response ->
                Log.d(tag, "PRIORITY_LOW:\n" + response?.body())
            }
        RetrofitManager.getRetrofit()
            .create(SampleApi::class.java)
            .contributors("square", "retrofit")
            .enqueueBlockCall(
                onResponse = { call: Call<String?>?, response: Response<String?> ->
                    Log.d(tag, "PRIORITY_VERY_HIGH:\n" + response.body())
                    mDialog.dismiss()
                    mTvResponse.text = response?.body()

                },
                onFailure = { call: Call<String?>?, t: Throwable ->

                    Log.d(tag, "onFailure:" + t.message)
                    mDialog.dismiss()
                    mTvResponse.text = t.message
                }
            )


        RetrofitManager.getRetrofit()
            .create(SampleApi::class.java)
            .contributors2("square", "retrofit")
            .enqueueBlockCall(
                TaskPriority.PRIORITY_HIGH,
                onResponse = { call: Call<String?>?, response: Response<String?> ->
                    Log.d(tag, "PRIORITY_VERY_HIGH:\n" + response.body())
                    mDialog.dismiss()
                    mTvResponse.text = response?.body()

                },
                onFailure = { call: Call<String?>?, t: Throwable ->

                    Log.d(tag, "onFailure:" + t.message)
                    mDialog.dismiss()
                    mTvResponse.text = t.message
                })

        val response=   RetrofitManager.getRetrofit()
        .create(SampleApi::class.java)
            .contributors2("square", "retrofit")
            .executeBlockCall(TaskPriority.PRIORITY_HIGH)

    }

    override fun onStop() {
        super.onStop()
        mDialog.dismiss()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 123 && grantResults.isNotEmpty()
            && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            request()
        }
    }
}



