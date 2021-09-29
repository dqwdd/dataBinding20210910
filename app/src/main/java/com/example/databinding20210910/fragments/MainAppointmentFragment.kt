package com.example.databinding20210910.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.databinding20210910.BaseActivity
import com.example.databinding20210910.EditAppoinmentActivity
import com.example.databinding20210910.R
import com.example.databinding20210910.adapters.AppointmentRecyclerAdapter
import com.example.databinding20210910.databinding.FragmentMainAppointmentBinding
import com.example.databinding20210910.databinding.FragmentMyFriendsListBinding
import com.example.databinding20210910.datas.AppointmentData
import com.example.databinding20210910.datas.BasicResponse
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainAppointmentFragment : BaseFragment() {

    companion object {
        private var frag : MainAppointmentFragment? = null
        fun getFrag() : MainAppointmentFragment {
            if ( frag == null ) {
                frag = MainAppointmentFragment()
            }
            return frag!!
        }
    }


    val mAppointmentList = ArrayList<AppointmentData>()

    lateinit var mRecyclerAdapter : AppointmentRecyclerAdapter

    lateinit var binding : FragmentMainAppointmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_appointment, container, false)
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupEvents()
        setValues()
    }


    override fun setupEvents() {
        binding.addAppointmentBtn.setOnClickListener {
            val myIntent = Intent(mContext, EditAppoinmentActivity::class.java)
            startActivity(myIntent)
        }

    }



    override fun setValues() {

        mRecyclerAdapter = AppointmentRecyclerAdapter(mContext, mAppointmentList)
        binding.appointmentRecyclerView.adapter = mRecyclerAdapter

        binding.appointmentRecyclerView.layoutManager = LinearLayoutManager(mContext)
    }


    override fun onResume() {
        super.onResume()
        getAppointmentListFromServer()
    }



    fun getAppointmentListFromServer() {

        apiService.getRequestAppointmentList().enqueue(object : Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {

                if ( response.isSuccessful) {
                    val basicResponse = response.body()!!

                    mAppointmentList.clear()

//                약속 목록 변수에 서버가 알려준 약속 목록을 전부 추가
                    mAppointmentList.addAll( basicResponse.data.appointments )

//                어댑터 새로고침
                    mRecyclerAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
            }

        })


    }

}