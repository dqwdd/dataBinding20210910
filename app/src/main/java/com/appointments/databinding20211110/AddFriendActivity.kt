package com.appointments.databinding20211110

import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.appointments.databinding20211110.adapters.SearchUserRecyclerAdapter
import com.appointments.databinding20211110.databinding.ActivityAddFriendBinding
import com.appointments.databinding20211110.datas.BasicResponse
import com.appointments.databinding20211110.datas.UserData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddFriendActivity : BaseActivity() {

    lateinit var binding : ActivityAddFriendBinding

    val mSearchedUserList = ArrayList<UserData>()
    lateinit var mSearchedUserAdapter : SearchUserRecyclerAdapter

    val mMyFriendList = ArrayList<UserData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_friend)
        setupEvent()
        setValues()
    }

    override fun setupEvent() {

        binding.searchBtn.setOnClickListener {
            val inputKetword = binding.keywordEdt.text.toString()

//            validation - 2 자 이상
            if (inputKetword.length <  2) {
                Toast.makeText(mContext, "검색어를 2자 이상 입력해 주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            apiService.getRequestSearchUser(inputKetword).enqueue(object : Callback<BasicResponse> {
                override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                    if ( response.isSuccessful) {
                        val basicResponse = response.body()!!

                        mSearchedUserList.clear()
                        mSearchedUserList.addAll(basicResponse.data.users)

                        mSearchedUserAdapter.notifyDataSetChanged()

                    }
                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                    //
                }
            })



        }

    }

    override fun setValues() {

        txtTitle.text = "친구 검색/추가"

        mSearchedUserAdapter = SearchUserRecyclerAdapter(mContext, mSearchedUserList)
        binding.searchUserRecyclerView.adapter = mSearchedUserAdapter

        binding.searchUserRecyclerView.layoutManager = LinearLayoutManager(mContext)

    }

}