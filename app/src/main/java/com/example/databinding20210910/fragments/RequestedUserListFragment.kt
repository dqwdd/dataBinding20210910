package com.example.databinding20210910.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.databinding20210910.R
import com.example.databinding20210910.adapters.RequestUserRecyclerAdapter
import com.example.databinding20210910.databinding.FragmentRequestedUserListBinding
import com.example.databinding20210910.datas.BasicResponse
import com.example.databinding20210910.datas.UserData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RequestedUserListFragment : BaseFragment() {

    lateinit var binding : FragmentRequestedUserListBinding

    val mRequestedMyFriendList = ArrayList<UserData>()
    lateinit var mRequestedFriendAdapter : RequestUserRecyclerAdapter

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
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

    }

    override fun setValues() {

        mRequestedFriendAdapter = RequestUserRecyclerAdapter(mContext, mRequestedMyFriendList)
        binding.requestedFriendRecyclerView.adapter = mRequestedFriendAdapter

        binding.requestedFriendRecyclerView.layoutManager = LinearLayoutManager(mContext)

    }

    override fun onResume() {
        super.onResume()
        getMyFriendListFromServer()
    }

    fun getMyFriendListFromServer() {

        apiService.getRequestFriendList("requested").enqueue(object : Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if ( response.isSuccessful) {

                    val basicResponse = response.body()!!

                    mRequestedMyFriendList.clear()
                    mRequestedMyFriendList.addAll(basicResponse.data.friends)

                    mRequestedFriendAdapter.notifyDataSetChanged()

                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
            }
        })

    }

}