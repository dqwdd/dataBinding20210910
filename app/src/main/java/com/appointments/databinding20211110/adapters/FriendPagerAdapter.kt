package com.appointments.databinding20211110.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.appointments.databinding20211110.fragments.MyFriendsListFragment
import com.appointments.databinding20211110.fragments.RequestedUserListFragment

class FriendPagerAdapter (fm : FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "친구목록"
            else -> "친구요청"
        }
    }

    override fun getCount() = 2

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> MyFriendsListFragment.getFrag()
            else -> RequestedUserListFragment.getFrag()
        }
    }
}