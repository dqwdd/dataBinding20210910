package com.example.databinding20210910

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.databinding20210910.web.ServerAPI
import com.example.databinding20210910.web.ServerAPIService
import retrofit2.Retrofit

abstract class BaseActivity : AppCompatActivity() {

    lateinit var mContext: Context

//    모든 화면에 레트로핏 / API 서비스를 미리 만들어서 물려주자
//    그렇게 각각의 화면에서는 apiService 변수를 불러내서 사용만 하면 되도록 처리하자~
    private lateinit var retrofit: Retrofit
    lateinit var apiService : ServerAPIService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this

        retrofit = ServerAPI.getRetrofit()
        apiService = retrofit.create(ServerAPIService::class.java)

    }

    abstract fun setupEvent()
    abstract fun setValues()

}