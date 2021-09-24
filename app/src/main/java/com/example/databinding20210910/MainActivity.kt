package com.example.databinding20210910

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.databinding20210910.adapters.AppointmentAdapter
import com.example.databinding20210910.adapters.AppointmentRecyclerAdapter
import com.example.databinding20210910.adapters.MainViewPagerAdapter
import com.example.databinding20210910.databinding.ActivityMainBinding
import com.example.databinding20210910.datas.AppointmentData
import com.example.databinding20210910.datas.BasicResponse
import com.example.databinding20210910.utils.GlobalData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : BaseActivity() {

    lateinit var mainViewPagerAdapter : MainViewPagerAdapter


    lateinit var binding: ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setupEvent()
        setValues()
    }


    override fun setupEvent() {

        profileImg.setOnClickListener {
            val myIntent = Intent(mContext, MySettinsgActivity::class.java)
            startActivity(myIntent)
        }

    }

    override fun setValues() {

        mainViewPagerAdapter = MainViewPagerAdapter( supportFragmentManager )
        binding.mainViewPager.adapter = mainViewPagerAdapter

        binding.mainTabLayout.setupWithViewPager( binding.mainViewPager )



        Toast.makeText(mContext, "${GlobalData.loginUser!!.nickName}님 환영합니다!", Toast.LENGTH_SHORT).show()

//        getAppointmentListFromServer()


        //상속받은 액션바에 있는 프로필버튼 보여주기
        profileImg.visibility = View.VISIBLE

        //메인화면의 화면 제목 변경
        titleTxt.text = "메인화면"

    }



}
//http://3.36.146.152/api/docs/