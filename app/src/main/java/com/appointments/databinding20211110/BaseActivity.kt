package com.appointments.databinding20211110

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.appointments.databinding20211110.datas.BasicResponse
import com.appointments.databinding20211110.utils.FontChanger
import com.appointments.databinding20211110.web.ServerAPI
import com.appointments.databinding20211110.web.ServerAPIService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

abstract class BaseActivity : AppCompatActivity() {

    lateinit var mContext: Context

//    모든 화면에 레트로핏 / API 서비스를 미리 만들어서 물려주자
//    그렇게 각각의 화면에서는 apiService 변수를 불러내서 사용만 하면 되도록 처리하자~
    private lateinit var retrofit: Retrofit
    lateinit var apiService : ServerAPIService

    lateinit var txtTitle: TextView
    lateinit var btnAdd: ImageView
    lateinit var btnClose: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this

        retrofit = ServerAPI.getRetrofit(mContext)
        apiService = retrofit.create(ServerAPIService::class.java)

        supportActionBar?.let {
            setCustomActionBar()
        }


    }

    override fun onStart() {
        super.onStart()

        // (액티비티 최상위 테그) rootView를 받아와서 폰트변경기에 의뢰
        val rootView = window.decorView.rootView//껍데기
        FontChanger.setGlobalFont(mContext, rootView)

    }


    abstract fun setupEvent(
    )

    abstract fun setValues()

    fun setCustomActionBar() {
        val defActionBar = supportActionBar!!

        defActionBar.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        defActionBar.setCustomView(R.layout.my_custom_action_bar)
        defActionBar.elevation = 0F

        val toolBar = defActionBar.customView.parent as Toolbar
        toolBar.setContentInsetsAbsolute(0, 0)

        txtTitle = defActionBar.customView.findViewById(R.id.txt_title)
        btnAdd = defActionBar.customView.findViewById(R.id.btn_add)
        btnClose = defActionBar.customView.findViewById(R.id.btn_close)
    }


}