package com.example.databinding20210910.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.databinding20210910.fragments.MainAppointmentFragment
import com.example.databinding20210910.fragments.MainInvitedAppointmentFragment

class MainViewPagerAdapter( fm : FragmentManager )  : FragmentPagerAdapter(fm) {

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position) {
            0 -> "약속목록"
            else -> "초대된 목록"
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        return when( position ) {
            0 -> MainAppointmentFragment()
            else -> MainInvitedAppointmentFragment()
        }
    }

}