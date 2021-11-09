package com.example.finalproject.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.appointments.databinding20211110.AddFriendActivity
import com.appointments.databinding20211110.R
import com.appointments.databinding20211110.databinding.FragmentFriendBinding
import com.appointments.databinding20211110.fragments.BaseFragment
import com.example.finalproject.adapters.FriendsListViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class FriendFragment : BaseFragment(){

    lateinit var binding: FragmentFriendBinding
    lateinit var mAdapter: FriendsListViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_friend, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setValues()
        setupEvents()
    }

    override fun setupEvents() {
        btnFindFriend.setOnClickListener {
            startActivity(Intent(mContext, AddFriendActivity::class.java))
        }


    }

    override fun setValues() {
        txtTitle.text = "친구"
        btnFindFriend.visibility = View.VISIBLE

        binding.vpFriend.adapter = FriendsListViewPagerAdapter(this)

        TabLayoutMediator(binding.tabFriend, binding.vpFriend) { tab, position ->
            when(position){
                0-> tab.text = "친구목록"
                else-> tab.text = "친구요청"
            }
        }.attach()
    }
}