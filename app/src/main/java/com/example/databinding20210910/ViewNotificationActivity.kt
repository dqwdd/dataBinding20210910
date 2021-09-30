package com.example.databinding20210910

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.databinding20210910.adapters.NotificationRecyclerAdapter
import com.example.databinding20210910.databinding.ActivityViewNotificationBinding
import com.example.databinding20210910.datas.BasicResponse
import com.example.databinding20210910.datas.DataResponse
import com.example.databinding20210910.datas.NotificationData
import com.example.databinding20210910.utils.FontChanger
import com.example.databinding20210910.web.ServerAPI
import com.example.databinding20210910.web.ServerAPIService
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.Exception

class ViewNotificationActivity : AppCompatActivity() {

    lateinit var binding : ActivityViewNotificationBinding

    val mMyNotificationList = ArrayList<NotificationData>()
    lateinit var mNotificationAdapter : NotificationRecyclerAdapter




    lateinit var mContext: Context

    //    모든 화면에 레트로핏 / API 서비스를 미리 만들어서 물려주자
//    그렇게 각각의 화면에서는 apiService 변수를 불러내서 사용만 하면 되도록 처리하자~
    private lateinit var retrofit: Retrofit
    lateinit var apiService : ServerAPIService

    //액션바에 있는 UI요소들을 상속시켜주자
    lateinit var profileImg : ImageView
    lateinit var titleTxt : TextView
    lateinit var editPlaceImg : ImageView
    lateinit var logoImg : ImageView
    lateinit var alarmIcon : ImageView
    lateinit var alarmIconRed : ImageView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_notification)



        mContext = this

        retrofit = ServerAPI.getRetrofit(mContext)
        apiService = retrofit.create(ServerAPIService::class.java)

        supportActionBar?.let {
            setCustomActionBar()
        }


        setValues()
        setupEvent()
    }

    override fun onStart() {
        super.onStart()

        // (액티비티 최상위 테그) rootView를 받아와서 폰트변경기에 의뢰
        val rootView = window.decorView.rootView//껍데기
        FontChanger.setGlobalFont(mContext, rootView)

    }



    fun setupEvent() {

        binding.readBtn.setOnClickListener {
            notificationRead()

        }

    }

    fun setCustomActionBar() {
        val defActionBar = supportActionBar!!

        defActionBar.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        defActionBar.setCustomView(R.layout.my_custom_action_bar)

        val toolBar = defActionBar.customView.parent as Toolbar
        toolBar.setContentInsetsAbsolute(0,0)

        profileImg = defActionBar.customView.findViewById<ImageView>(R.id.profileImg)
        titleTxt = defActionBar.customView.findViewById<TextView>(R.id.titleTxt)
        editPlaceImg = defActionBar.customView.findViewById<ImageView>(R.id.editPlaceImg)
        logoImg = defActionBar.customView.findViewById<ImageView>(R.id.logoImg)
        alarmIcon = defActionBar.customView.findViewById<ImageView>(R.id.alarmIcon)
        alarmIconRed = defActionBar.customView.findViewById<ImageView>(R.id.alarmIconRed)



        alarmIcon.setOnClickListener {
            val myIntent = Intent(mContext, ViewNotificationActivity::class.java)
            startActivity(myIntent)
        }


    }

    fun setValues() {

        alarmIcon.visibility = View.GONE
        alarmIconRed.visibility = View.GONE

        titleTxt.text = "알림"

        mNotificationAdapter = NotificationRecyclerAdapter(mContext, mMyNotificationList)
        binding.notificationRecyclerView.adapter = mNotificationAdapter

        binding.notificationRecyclerView.layoutManager = LinearLayoutManager(mContext)


        getNotificationsFromServer()

   }


    private fun getNotificationsFromServer() {
        apiService.getRequestNotifications("true").enqueue(object : Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {

                if (response.isSuccessful) {
                    val basicResponse = response.body()!!
                    mMyNotificationList.clear()
                    mMyNotificationList.addAll(response.body()!!.data.notifications)

                    /*
                    binding.rvNotificationList.apply{
                        adapter = NotificationAdapter(mContext, mNotificationList)
                        layoutManager =
                            LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
                    }
                    *///프레그먼트일 때 이거 쓰나봄

                    mNotificationAdapter.notifyDataSetChanged()

                    notificationRead()
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }
        })
    }


    fun notificationRead() {

        apiService.postRequestNotificatioRead(10000).enqueue(object : Callback<BasicResponse>{
            override fun onResponse(
                call: Call<BasicResponse>,
                response: Response<BasicResponse>
            ) {
                try {

                    val intent = intent
                    finish() //현재 액티비티 종료 실시
                    overridePendingTransition(0, 0) //인텐트 애니메이션 없애기
                    startActivity(intent) //현재 액티비티 재실행 실시
                    overridePendingTransition(0, 0) //인텐트 애니메이션 없애기

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
            }
        })


    }



}