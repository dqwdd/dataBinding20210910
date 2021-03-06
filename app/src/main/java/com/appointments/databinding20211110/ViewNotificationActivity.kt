package com.appointments.databinding20211110

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.appointments.databinding20211110.adapters.NotificationRecyclerAdapter
import com.appointments.databinding20211110.databinding.ActivityViewNotificationBinding
import com.appointments.databinding20211110.datas.BasicResponse
import com.appointments.databinding20211110.datas.NotificationData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewNotificationActivity : BaseActivity() {

    lateinit var binding : ActivityViewNotificationBinding

    val mMyNotificationList = ArrayList<NotificationData>()
    lateinit var mNotificationAdapter : NotificationRecyclerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_notification)
        setValues()
        setupEvent()
    }

    override fun setupEvent() {
    }


    override fun setValues() {

        txtTitle.text = "알림"

        mNotificationAdapter = NotificationRecyclerAdapter(mContext, mMyNotificationList)
        binding.notificationRecyclerView.adapter = mNotificationAdapter

        binding.notificationRecyclerView.layoutManager = LinearLayoutManager(mContext)


        getNotificationsFromServer()

   }


    //노티 가져오기
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


    //노티 읽기 처리
    fun notificationRead() {

        apiService.postRequestNotificatioRead(10000).enqueue(object : Callback<BasicResponse>{
            override fun onResponse(
                call: Call<BasicResponse>,
                response: Response<BasicResponse>
            ) {

                /*
                try {

                    val intent = intent
                    finish() //현재 액티비티 종료 실시
                    overridePendingTransition(0, 0) //인텐트 애니메이션 없애기
                    startActivity(intent) //현재 액티비티 재실행 실시
                    overridePendingTransition(0, 0) //인텐트 애니메이션 없애기

                } catch (e: Exception) {
                    e.printStackTrace()
                }
                */

            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
            }
        })


    }



}