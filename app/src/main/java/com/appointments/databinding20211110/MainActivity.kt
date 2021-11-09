package com.appointments.databinding20211110

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.appointments.databinding20211110.adapters.*
import com.appointments.databinding20211110.databinding.ActivityMainBinding
import com.appointments.databinding20211110.fragments.MainAppointmentFragment
import com.appointments.databinding20211110.fragments.MainInvitedAppointmentFragment
import com.appointments.databinding20211110.utils.GlobalData
import com.example.finalproject.fragments.FriendFragment
import com.example.finalproject.fragments.NotificationFragment
import com.example.finalproject.fragments.SettingFragment

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

    //2번 백클릭 시 종료, 딜레이 2초
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

    override fun setupEvent() {
    }


    override fun setValues() {

        replaceFragment(MainAppointmentFragment())

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.menu_my_appointment -> replaceFragment(MainAppointmentFragment())
                R.id.menu_invite_appointment -> replaceFragment(MainInvitedAppointmentFragment())
                R.id.menu_friend -> replaceFragment(FriendFragment())
                R.id.menu_notification -> replaceFragment(NotificationFragment())
                R.id.menu_setting -> replaceFragment(SettingFragment())
                else -> { }
            }
            true
        }

        setNotificationBadge()

        Toast.makeText(mContext, "${GlobalData.loginUser!!.nickName}님 환영합니다", Toast.LENGTH_SHORT).show()
    }

    private fun setNotificationBadge(){
        binding.bottomNavigation.getOrCreateBadge(R.layout.my_custom_notification_badge).number = 3
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.framelayout_main, fragment)
        fragmentTransaction.commit()
    }

}
//http://3.36.146.152/api/docs/
//https://keepthetime.xyz/api/docs/