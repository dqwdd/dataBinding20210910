package com.example.databinding20210910.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.databinding20210910.R
import com.example.databinding20210910.adapters.MyFriendsRecyclerAdapter
import com.example.databinding20210910.databinding.FragmentMyFriendsListBinding
import com.example.databinding20210910.datas.BasicResponse
import com.example.databinding20210910.datas.UserData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyFriendsListFragment : BaseFragment() {

    lateinit var binding : FragmentMyFriendsListBinding

    val mMyFriendList = ArrayList<UserData>()
    lateinit var mFriendAdapter : MyFriendsRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_friends_list, container, false)
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

        mFriendAdapter = MyFriendsRecyclerAdapter(mContext, mMyFriendList)
        binding.myFriendsRecyclerView.adapter = mFriendAdapter

    }

    override fun onResume() {
        super.onResume()
        getMyFriendListFromServer()
    }

    fun getMyFriendListFromServer() {

        apiService.getRequestFriendList("my").enqueue(object : Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if ( response.isSuccessful) {

                    val basicResponse = response.body()!!

                    mMyFriendList.clear()
                    mMyFriendList.addAll(basicResponse.data.friends)

                    mFriendAdapter.notifyDataSetChanged()

                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                //
            }
        })

    }

}