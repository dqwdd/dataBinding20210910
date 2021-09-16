package com.example.databinding20210910

import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.databinding20210910.adapters.SearchUserRecyclerAdapter
import com.example.databinding20210910.databinding.ActivityAddFriendBinding
import com.example.databinding20210910.datas.BasicResponse
import com.example.databinding20210910.datas.UserData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddFriendActivity : BaseActivity() {

    lateinit var binding : ActivityAddFriendBinding

    val mSearchList = ArrayList<UserData>()
    lateinit var mFriendAdapter : SearchUserRecyclerAdapter

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

            apiService.getRequestSearchUser("my").enqueue(object : Callback<BasicResponse> {
                override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                    if ( response.isSuccessful) {
                        val basicResponse = response.body()!!

                        mSearchList.clear()
                        mSearchList.addAll(basicResponse.data.users)

                        mFriendAdapter.notifyDataSetChanged()

                    }
                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                    //
                }
            })



        }

    }

    override fun setValues() {
    }

}