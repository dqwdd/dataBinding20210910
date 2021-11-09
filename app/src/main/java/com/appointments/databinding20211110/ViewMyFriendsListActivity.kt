package com.appointments.databinding20211110

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.appointments.databinding20211110.adapters.FriendPagerAdapter
import com.appointments.databinding20211110.databinding.ActivityViewMyFriendsListBinding
import com.appointments.databinding20211110.fragments.MyFriendsListFragment
import com.appointments.databinding20211110.fragments.RequestedUserListFragment

class ViewMyFriendsListActivity : BaseActivity() {

    lateinit var binding : ActivityViewMyFriendsListBinding
    lateinit var mFPA : FriendPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_my_friends_list)
        setupEvent()
        setValues()
    }

    override fun setupEvent() {

        binding.friendsViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
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
                        (mFPA.getItem(position) as MyFriendsListFragment).getMyFriendListFromServer()
                    }
                    else -> {
                        (mFPA.getItem(position) as RequestedUserListFragment).getRequestUserListFromServer()
                    }
                }


            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })



    }

    override fun setValues() {

        txtTitle.text = "친구"

        mFPA = FriendPagerAdapter( supportFragmentManager )
        binding.friendsViewPager.adapter = mFPA

        binding.friendsTabLayout.setupWithViewPager(binding.friendsViewPager)

    }
}