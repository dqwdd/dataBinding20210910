package com.example.databinding20210910.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.databinding20210910.*
import com.example.databinding20210910.datas.AppointmentData
import com.example.databinding20210910.datas.BasicResponse
import com.example.databinding20210910.fragments.MainAppointmentFragment
import com.example.databinding20210910.utils.FontChanger
import com.example.databinding20210910.web.ServerAPI
import com.example.databinding20210910.web.ServerAPIService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat

class AppointmentRecyclerAdapter(
    val mContext: Context,
    val mList: List<AppointmentData>) : RecyclerView.Adapter<AppointmentRecyclerAdapter.AppointmentViewHolder>() {

    inner class AppointmentViewHolder(view: View) : BaseViewHolder(mContext, view) {

        //view: View, val adapater: AppointmentRecyclerAdapter

        val titleTxt = view.findViewById<TextView>(R.id.titleTxt)
        val dateTimeTxt = view.findViewById<TextView>(R.id.dateTimeTxt)
        val placeNameTxt = view.findViewById<TextView>(R.id.placeNameTxt)
        val viewPlaceMapBtn = view.findViewById<ImageView>(R.id.viewPlaceMapBtn)
        val rootLayout = view.findViewById<LinearLayout>(R.id.rootLayout)


        val dateTimeSDF = SimpleDateFormat("M/d a h:mm")

        fun bind(//context: Context,   //여기의 mContext는 inner 쓰기 전엔 context였음
                 data: AppointmentData ) {
            titleTxt.text = data.title

//            약속 일시를 : Date형태로 파싱됨 -> String으로 가공해야 함 -> SimpleDateFormat을 사용해야 함
            dateTimeTxt.text = data.getFormattedDateTime()
            placeNameTxt.text = data.placeName


            // 이벤트 처리들

            viewPlaceMapBtn.setOnClickListener {
                val myIntent = Intent(mContext, ViewMapActivity::class.java)
                myIntent.putExtra("appointment", data)
                mContext.startActivity(myIntent)
            }


            rootLayout.setOnClickListener {
                val myIntent = Intent(mContext, ViewAppointmentDetailActivity::class.java)
                myIntent.putExtra("appointment", data)
                mContext.startActivity(myIntent)
            }


            rootLayout.setOnLongClickListener {

                val alert = AlertDialog.Builder(mContext)
                alert.setMessage("정말 약속을 삭제하시겠습니까?")
                alert.setNegativeButton("확인", DialogInterface.OnClickListener { dialogInterface, i ->

//                해당 답글 삭제 -> API 요청 + 새로고침
                    val apiService = ServerAPI.getRetrofit(mContext).create(ServerAPIService::class.java)

                    apiService.getRequestAppointmentDelete(data.id).enqueue(object :
                        Callback<BasicResponse> {
                        override fun onResponse(
                            call: Call<BasicResponse>,
                            response: Response<BasicResponse>
                        ) {
                            if (response.isSuccessful) {
                                Log.d("서버응답", "")
                                Toast.makeText(mContext, "약속이 삭제되었습니다", Toast.LENGTH_SHORT).show()
                                ((mContext as MainActivity)
                                    .mainViewPagerAdapter.getItem(0) as MainAppointmentFragment)
                                    .getAppointmentListFromServer()
                                //((context as ViewMyFriendsListActivity)
                                // .mFPA.getItem(1) as RequestedUserListFragment)
                                //\.getRequestUserListFromServer()
                            }
                            else {
                                Toast.makeText(mContext, "자신의 약속만 삭제할 수 있습니다", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                        }
                    })
                })
                alert.setPositiveButton("취소", null)
                alert.show()
                return@setOnLongClickListener true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.appointment_list_item, parent, false)
        FontChanger.setGlobalFont(mContext, view)
        return AppointmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        val data = mList[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

}