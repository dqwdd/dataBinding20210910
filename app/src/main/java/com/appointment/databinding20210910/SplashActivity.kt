package com.appointment.databinding20210910

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.appointment.databinding20210910.datas.BasicResponse
import com.appointment.databinding20210910.utils.GlobalData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        setupEvent()
        setValues()
    }

    override fun setupEvent() {
    }

    override fun setValues() {

        GlobalData.context = mContext


        apiService.getRequestMyInfo().enqueue(object : Callback<BasicResponse> {
            override fun onResponse(
                call: Call<BasicResponse>,
                response: Response<BasicResponse>
            ) {

                if ( response.isSuccessful ) {
                    val basicResponse = response.body()!!
                    GlobalData.loginUser = basicResponse.data.user
                    Log.d("SplashActivity", "")
                    Log.d("SplashActivity", "")
                }

            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
            }
        })



        val myHandler = Handler(Looper.getMainLooper())
        myHandler.postDelayed({

            val myIntent: Intent

//            2.5초를 기다린 후에, 내 정보를 받아온 결과를 확인해보자.
//            성공적이었을때만 사용자 정보가 대입됨.

            if (GlobalData.loginUser != null) {
                myIntent = Intent(mContext, MainActivity::class.java)
            }
            else {
                myIntent = Intent(mContext, LoginActivity::class.java)
            }
            startActivity(myIntent)


        }, 2500)

    }
}