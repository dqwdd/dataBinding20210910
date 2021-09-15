package com.example.databinding20210910

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.databinding20210910.adapters.MyPlaceRecyclerAdapter
import com.example.databinding20210910.databinding.ActivityViewMyPlaceListBinding
import com.example.databinding20210910.datas.BasicResponse
import com.example.databinding20210910.datas.PlaceData
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

        editPlaceImg.setOnClickListener {
            val myIntent = Intent(mContext, EditMyPlaceActivity::class.java)
            startActivity(myIntent)
        }

    }

    override fun setValues() {

        titleTxt.text = "내가 자주 쓰는 출발 장소들"

        editPlaceImg.visibility = View.VISIBLE

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