package com.appointment.databinding20210910

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.NetworkOnMainThreadException
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.appointment.databinding20210910.adapters.*
import com.appointment.databinding20210910.databinding.ActivityMainBinding
import com.appointment.databinding20210910.fragments.MainAppointmentFragment
import com.appointment.databinding20210910.fragments.MainInvitedAppointmentFragment

class MainActivity : BaseActivity() {

    lateinit var mainViewPagerAdapter : MainViewPagerAdapter

    lateinit var binding: ActivityMainBinding


    private var doubleBackToExit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setupEvent()
        setValues()
        onBackPressed()
    }


    fun runDelayed(millis: Long, function: () -> Unit) {
        Handler(Looper.getMainLooper()).postDelayed(function, millis)
    }

    override fun onBackPressed() {
        if (doubleBackToExit) {
            finishAffinity()
        } else {
            doubleBackToExit = true
            runDelayed(2000L) {
                doubleBackToExit = false
            }
        }
    }

    override fun onResume() {
        super.onResume()

        (mContext as BaseActivity).notification()

    }

    override fun setupEvent() {


        binding.mainViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
                // 오프셋==옆으로 얼마나 이동했냐~ 로그로 찍어보면(positionOffset을) 소수점단위로 보여주는데
//            뭐 옆으로 얼마만큼 움직였을 때 실행되게 할꺼면 쓰는거임
            ) {
            }

            override fun onPageSelected(position: Int) {
                Log.d("선택된 페이지", position.toString())
//                각 페이지에 맞는 프래그먼트의 새로고침 실행

                when (position) {
                    0 -> {
                        (mainViewPagerAdapter.getItem(position) as MainAppointmentFragment)
                            .getAppointmentListFromServer()
                    }
                    else -> {
                        (mainViewPagerAdapter.getItem(position) as MainInvitedAppointmentFragment)
                            .getInvitedAppointmentListFromServer()
                    }
                }


            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })




        profileImg.setOnClickListener {
            val myIntent = Intent(mContext, MySettinsgActivity::class.java)
            startActivity(myIntent)
        }
    }


    override fun setValues() {


        //상속받은 액션바에 있는 프로필버튼 보여주기
        profileImg.visibility = View.VISIBLE

        //메인화면의 화면 제목 변경
        titleTxt.text = "메인화면"



        mainViewPagerAdapter = MainViewPagerAdapter( supportFragmentManager )
        binding.mainViewPager.adapter = mainViewPagerAdapter

        binding.mainTabLayout.setupWithViewPager(binding.mainViewPager)

    }

}
//http://3.36.146.152/api/docs/
//https://keepthetime.xyz/api/docs/