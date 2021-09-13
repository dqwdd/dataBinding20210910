package com.example.databinding20210910

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.databinding20210910.datas.BasicResponse
import com.example.databinding20210910.utils.ContextUtil
import com.example.databinding20210910.utils.GlobalData
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

        val myHandler = Handler(Looper.getMainLooper())
        myHandler.postDelayed({

            val myIntent: Intent

            //

            if (ContextUtil.getToken(mContext) != "") {f

                apiService.getRequestMyInfo().enqueue(object : Callback<BasicResponse> {
                    override fun onResponse(
                        call: Call<BasicResponse>,
                        response: Response<BasicResponse>
                    ) {

                        if ( response.isSuccessful ) {
                            val basicResponse = response.body()!!
                            GlobalData.loginUser = basicResponse.data.user
                        }

                    }

                    override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                    }
                })

//                GlobalData.loginUser = ?

                myIntent = Intent(mContext, MainActivity::class.java)
            } else {
                myIntent = Intent(mContext, LoginActivity::class.java)
            }

            startActivity(myIntent)


        }, 2500)

    }
}