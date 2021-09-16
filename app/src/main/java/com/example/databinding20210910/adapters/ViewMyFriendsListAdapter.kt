package com.example.databinding20210910.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.databinding20210910.fragments.MyFriendsListFragment
import com.example.databinding20210910.fragments.RequestedUserListFragment

class ViewMyFriendsListAdapter ( fm : FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "이름"
            1 -> "인사"
            else -> "출생년도"
        }
    }

    override fun getCount(): Int {
        return 3

    }
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> MyFriendsListFragment()
//            1 -> NameFragment()
            else -> RequestedUserListFragment()
        }
    }
}