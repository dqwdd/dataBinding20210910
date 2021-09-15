package com.example.databinding20210910

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.databinding20210910.adapters.AppointmentAdapter
import com.example.databinding20210910.databinding.ActivityMainBinding
import com.example.databinding20210910.datas.AppointmentData
import com.example.databinding20210910.datas.BasicResponse
import com.example.databinding20210910.utils.GlobalData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : BaseActivity() {

    lateinit var binding: ActivityMainBinding

    val mAppointmentList = ArrayList<AppointmentData>()
    lateinit var mAdapter : AppointmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setupEvent()
        setValues()
    }

    override fun onResume() {
        super.onResume()
        getAppointmentListFromServer()
//        setValues에서 실행하던거 여기서 실행
    }

    override fun setupEvent() {

        binding.addAppointmentBtn.setOnClickListener {
            val myIntent = Intent(mContext, EditAppoinmentActivity::class.java)
            startActivity(myIntent)
        }

        profileImg.setOnClickListener {
            val myIntent = Intent(mContext, MySettinsgActivity::class.java)
            startActivity(myIntent)
        }

    }

    override fun setValues() {

        Toast.makeText(mContext, "${GlobalData.loginUser!!.nickName}님 환영합니다!", Toast.LENGTH_SHORT).show()

//        getAppointmentListFromServer()

        mAdapter = AppointmentAdapter(mContext, R.layout.appointment_list_item, mAppointmentList)
        binding.appointmentListView.adapter = mAdapter

        //상속받은 액션바에 있는 프로필버튼 보여주기
        profileImg.visibility = View.VISIBLE

        //메인화면의 화면 제목 변경
        titleTxt.text = "메인화면"

    }

    fun getAppointmentListFromServer() {

        apiService.getRequestAppointmentList().enqueue(object : Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {

                val basicResponse = response.body()!!

                mAppointmentList.clear()

//                약속 목록 변수에 서버가 알려준 약속 목록을 전부 추가
                mAppointmentList.addAll( basicResponse.data.appointments )

//                어댑터 새로고침
                mAdapter.notifyDataSetChanged()
                }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
            }

        })


    }

}
//http://3.36.146.152/api/docs/