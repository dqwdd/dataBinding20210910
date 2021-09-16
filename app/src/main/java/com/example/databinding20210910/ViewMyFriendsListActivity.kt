package com.example.databinding20210910

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.databinding20210910.adapters.FriendPagerAdapter
import com.example.databinding20210910.databinding.ActivityViewMyFriendsListBinding

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
    }

    override fun setValues() {

        titleTxt.text = "친구"

        mFPA = FriendPagerAdapter( supportFragmentManager )
        binding.friendsViewPager.adapter = mFPA

        binding.friendsTabLayout.setupWithViewPager(binding.friendsViewPager)

    }
}