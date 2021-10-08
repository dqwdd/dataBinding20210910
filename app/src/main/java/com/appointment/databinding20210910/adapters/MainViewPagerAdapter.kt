package com.appointment.databinding20210910.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.appointment.databinding20210910.fragments.MainAppointmentFragment
import com.appointment.databinding20210910.fragments.MainInvitedAppointmentFragment

class MainViewPagerAdapter( fm : FragmentManager )  : FragmentPagerAdapter(fm) {

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "약속 목록"
            else -> "초대된 목록"
        }
    }

    override fun getCount() = 2

    override fun getItem(position: Int): Fragment {
        return when( position ) {
            0 -> MainAppointmentFragment.getFrag()
            else -> MainInvitedAppointmentFragment.getFrag()
        }
    }


}