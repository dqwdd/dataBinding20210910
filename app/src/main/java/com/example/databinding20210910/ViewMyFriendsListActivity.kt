package com.example.databinding20210910

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.databinding20210910.adapters.ViewMyFriendsListAdapter
import com.example.databinding20210910.databinding.ActivityViewMyFriendsListBinding

class ViewMyFriendsListActivity : BaseActivity() {

    lateinit var binding : ActivityViewMyFriendsListBinding
    lateinit var vmfla : ViewMyFriendsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_my_friends_list)
        setupEvent()
        setValues()
    }

    override fun setupEvent() {
    }

    override fun setValues() {

        vmfla = ViewMyFriendsListAdapter( supportFragmentManager )
        binding.friendsViewPager.adapter = vmfla

        binding.friendsTabLayout.setupWithViewPager(binding.friendsViewPager)

    }
}