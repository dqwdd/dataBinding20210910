package com.appointments.databinding20210910.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.appointments.databinding20210910.utils.FontChanger
import com.appointments.databinding20210910.web.ServerAPI
import com.appointments.databinding20210910.web.ServerAPIService
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


        //모든 텍스트뷰의 폰트 변경
        FontChanger.setGlobalFont(mContext, requireView())
        //requireView() == 프래그먼트의 최상위 레이아웃


    }

    abstract fun setupEvents()
    abstract fun setValues()


}