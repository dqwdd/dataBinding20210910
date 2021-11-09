package com.appointments.databinding20211110.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.appointments.databinding20211110.R
import com.appointments.databinding20211110.adapters.MyFriendsRecyclerAdapter
import com.appointments.databinding20211110.databinding.FragmentMyFriendsListBinding
import com.appointments.databinding20211110.datas.BasicResponse
import com.appointments.databinding20211110.datas.UserData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyFriendsListFragment : BaseFragment() {

    companion object {
        private var frag : MyFriendsListFragment? = null
        fun getFrag() : MyFriendsListFragment {
            if ( frag == null ) {
                frag = MyFriendsListFragment()
            }
            return frag!!
        }
    }

    lateinit var binding : FragmentMyFriendsListBinding

    val mMyFriendList = ArrayList<UserData>()
    lateinit var mFriendAdapter : MyFriendsRecyclerAdapter
    val arr = ArrayList<String>()

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

        txtTitle.text = "친구"

        mFriendAdapter = MyFriendsRecyclerAdapter(mContext, mMyFriendList)
        binding.myFriendsRecyclerView.adapter = mFriendAdapter

        binding.myFriendsRecyclerView.layoutManager = LinearLayoutManager(mContext)

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