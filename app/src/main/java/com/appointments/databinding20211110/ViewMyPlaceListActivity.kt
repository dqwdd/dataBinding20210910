package com.appointments.databinding20211110

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.appointments.databinding20211110.adapters.MyPlaceRecyclerAdapter
import com.appointments.databinding20211110.databinding.ActivityViewMyPlaceListBinding
import com.appointments.databinding20211110.datas.BasicResponse
import com.appointments.databinding20211110.datas.PlaceData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewMyPlaceListActivity : BaseActivity() {

    lateinit var binding : ActivityViewMyPlaceListBinding

    val mMyPlaceList = ArrayList<PlaceData>()

    lateinit var mPlaceAdapter : MyPlaceRecyclerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_my_place_list)
        setupEvent()
        setValues()
    }

    override fun setupEvent() {
    }

    override fun setValues() {

        txtTitle.text = "내가 자주 쓰는 출발 장소들"

        binding.myPlaceRecyclerView.layoutManager = LinearLayoutManager(mContext)

        mPlaceAdapter = MyPlaceRecyclerAdapter(mContext, mMyPlaceList)

        binding.myPlaceRecyclerView.adapter = mPlaceAdapter

    }

    override fun onResume() {
        super.onResume()
        getMyPlaceListFromServer()
    }


    fun getMyPlaceListFromServer() {
        apiService.getRequestMyPlaceList().enqueue(object : Callback<BasicResponse>{
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {

                if (response.isSuccessful) {
                    val basicResponse = response.body()!!

                    mMyPlaceList.clear()

                    mMyPlaceList.addAll( basicResponse.data.places )

                    mPlaceAdapter.notifyDataSetChanged()

                }

            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }
        })
    }

}