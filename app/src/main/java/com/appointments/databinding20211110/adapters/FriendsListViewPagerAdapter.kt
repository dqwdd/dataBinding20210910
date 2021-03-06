package com.example.finalproject.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.appointments.databinding20211110.fragments.MyFriendsListFragment
import com.appointments.databinding20211110.fragments.RequestedUserListFragment

class FriendsListViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> MyFriendsListFragment()
            else -> RequestedUserListFragment()
        }
}