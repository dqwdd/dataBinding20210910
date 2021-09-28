package com.example.databinding20210910

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.databinding20210910.adapters.NotificationRecyclerAdapter
import com.example.databinding20210910.databinding.ActivityViewNotificationBinding
import com.example.databinding20210910.datas.BasicResponse
import com.example.databinding20210910.datas.DataResponse
import com.example.databinding20210910.datas.NotificationData
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewNotificationActivity : BaseActivity() {

    lateinit var binding : ActivityViewNotificationBinding


    val mMyNotificationList = ArrayList<DataResponse>()
    lateinit var mNotificationAdapter : NotificationRecyclerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_notification)
        setValues()
        setupEvent()
    }

    override fun setupEvent() {

        binding.readBtn.setOnClickListener {

            apiService.postRequestNotificatioRead(100).enqueue(object : Callback<BasicResponse>{
                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {

                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                }
            })

        }

    }

    override fun onResume() {
        super.onResume()
    }

    override fun setValues() {

        alarmIcon.visibility = View.GONE
        alarmIconRed.visibility = View.INVISIBLE

        titleTxt.text = "알림"

        mNotificationAdapter = NotificationRecyclerAdapter(mContext, mMyNotificationList)
        binding.notificationRecyclerView.adapter = mNotificationAdapter

        binding.notificationRecyclerView.layoutManager = LinearLayoutManager(mContext)


   }




}