package com.example.databinding20210910.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.databinding20210910.R
import com.example.databinding20210910.databinding.FragmentRequestedUserListBinding
import com.example.databinding20210910.databinding.FragmentsMyFriendsListBinding

class RequestedUserListFragment : Fragment() {

    lateinit var binding : FragmentRequestedUserListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_requested_user_list, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }
}