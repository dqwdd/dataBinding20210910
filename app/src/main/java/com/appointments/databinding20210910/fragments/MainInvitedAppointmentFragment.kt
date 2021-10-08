package com.appointments.databinding20210910.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.appointments.databinding20210910.EditAppoinmentActivity
import com.appointments.databinding20210910.R
import com.appointments.databinding20210910.adapters.InvitedAppointmentRecyclerAdapter
import com.appointments.databinding20210910.databinding.FragmentMainInvitedAppointmentBinding
import com.appointments.databinding20210910.datas.AppointmentData
import com.appointments.databinding20210910.datas.BasicResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainInvitedAppointmentFragment : BaseFragment() {

    companion object {
        private var frag : MainInvitedAppointmentFragment? = null
        fun getFrag() : MainInvitedAppointmentFragment {
            if (frag == null) {
                frag = MainInvitedAppointmentFragment()
            }

            return frag!!
        }
    }

    val mInvitedAppointmentList = ArrayList<AppointmentData>()

    lateinit var mRecyclerAdapter : InvitedAppointmentRecyclerAdapter

    lateinit var binding : FragmentMainInvitedAppointmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_invited_appointment, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        getInvitedAppointmentListFromServer()
//        setValues에서 실행하던거 여기서 실행
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
        mRecyclerAdapter = InvitedAppointmentRecyclerAdapter(mContext, mInvitedAppointmentList)
        binding.appointmentRecyclerView.adapter = mRecyclerAdapter

        binding.appointmentRecyclerView.layoutManager = LinearLayoutManager(mContext)
    }


    fun getInvitedAppointmentListFromServer() {

        apiService.getRequestAppointmentList().enqueue(object : Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {

                val basicResponse = response.body()!!

                mInvitedAppointmentList.clear()

//                약속 목록 변수에 서버가 알려준 약속 목록을 전부 추가
                mInvitedAppointmentList.addAll( basicResponse.data.invited_appointments )

//                어댑터 새로고침
                mRecyclerAdapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
            }

        })


    }



}