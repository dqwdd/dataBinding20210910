package com.example.databinding20210910.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.databinding20210910.BaseActivity
import com.example.databinding20210910.web.ServerAPI
import com.example.databinding20210910.web.ServerAPIService
import retrofit2.Retrofit

abstract class BaseFragment : Fragment() {

    lateinit var mContext: Context

    private lateinit var retrofit: Retrofit
    lateinit var apiService : ServerAPIService

    /*lateinit var mainActivity: BaseActivity

    override fun onAttach(mContext: Context) { super.onAttach(mContext)
        mainActivity = mContext as BaseActivity
        //runOnUiThread쓰는 코드
    }*/

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mContext = requireContext()
        retrofit = ServerAPI.getRetrofit(mContext)
        apiService = retrofit.create(ServerAPIService::class.java)
    }

    abstract fun setupEvents()
    abstract fun setValues()


}