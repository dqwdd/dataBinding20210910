package com.appointments.databinding20211110.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.appointments.databinding20211110.R
import com.appointments.databinding20211110.adapters.RequestUserRecyclerAdapter
import com.appointments.databinding20211110.databinding.FragmentRequestedUserListBinding
import com.appointments.databinding20211110.datas.BasicResponse
import com.appointments.databinding20211110.datas.UserData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RequestedUserListFragment : BaseFragment() {

    companion object {
        private var frag : RequestedUserListFragment? = null
        fun getFrag() : RequestedUserListFragment {
            if ( frag == null ) {
                frag = RequestedUserListFragment()
            }
            return frag!!
        }
    }

    lateinit var binding : FragmentRequestedUserListBinding

    val mRequestUserList = ArrayList<UserData>()
    lateinit var mRequestUserAdapter : RequestUserRecyclerAdapter

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

        txtTitle.text = "친구"

        mRequestUserAdapter = RequestUserRecyclerAdapter(mContext, mRequestUserList)
        binding.requestedFriendRecyclerView.adapter = mRequestUserAdapter

        binding.requestedFriendRecyclerView.layoutManager = LinearLayoutManager(mContext)

    }

    override fun onResume() {
        super.onResume()
        getRequestUserListFromServer()
    }

    fun getRequestUserListFromServer() {

        apiService.getRequestFriendList("requested").enqueue(object : Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {

                if ( response.isSuccessful) {
                    val basicResponse = response.body()!!

                    mRequestUserList.clear()
                    mRequestUserList.addAll(basicResponse.data.friends)

                    mRequestUserAdapter.notifyDataSetChanged()

                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
            }
        })

    }

}