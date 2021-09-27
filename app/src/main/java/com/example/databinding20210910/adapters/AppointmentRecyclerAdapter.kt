package com.example.databinding20210910.adapters

import android.app.AlertDialog
import android.app.PendingIntent.getActivity
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
import com.example.databinding20210910.datas.PlaceData
import com.example.databinding20210910.fragments.MainAppointmentFragment
import com.example.databinding20210910.fragments.RequestedUserListFragment
import com.example.databinding20210910.web.ServerAPI
import com.example.databinding20210910.web.ServerAPIService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat

class AppointmentRecyclerAdapter(
    val mContext: Context,
    val mList: List<AppointmentData>) : RecyclerView.Adapter<AppointmentRecyclerAdapter.AppointmentViewHolder>() {

    class AppointmentViewHolder(val mContext: Context,view : View) : RecyclerView.ViewHolder(view) {

        val titleTxt = view.findViewById<TextView>(R.id.titleTxt)
        val dateTimeTxt = view.findViewById<TextView>(R.id.dateTimeTxt)
        val placeNameTxt = view.findViewById<TextView>(R.id.placeNameTxt)
        val viewPlaceMapBtn = view.findViewById<ImageView>(R.id.viewPlaceMapBtn)
        val rootLayout = view.findViewById<LinearLayout>(R.id.rootLayout)


        val dateTimeSDF = SimpleDateFormat("M/d a h:mm")

        fun bind( context: Context, data: AppointmentData ) {
            titleTxt.text = data.title

//            약속 일시를 : Date형태로 파싱됨 -> String으로 가공해야 함 -> SimpleDateFormat을 사용해야 함
            dateTimeTxt.text = data.getFormattedDateTime()
            placeNameTxt.text = data.placeName


            // 이벤트 처리들

            viewPlaceMapBtn.setOnClickListener {
                val myIntent = Intent(context, ViewMapActivity::class.java)
                myIntent.putExtra("appointment", data)
                context.startActivity(myIntent)
            }


            rootLayout.setOnClickListener {
                val myIntent = Intent(context, ViewAppointmentDetailActivity::class.java)
                myIntent.putExtra("appointment", data)
                context.startActivity(myIntent)
            }

            rootLayout.setOnLongClickListener {

                val apiService = ServerAPI.getRetrofit(context).create(ServerAPIService::class.java)


                val alert = AlertDialog.Builder(mContext)
                alert.setMessage("정말 약속을 삭제하시겠습니까?")
                alert.setNegativeButton("확인", DialogInterface.OnClickListener { dialogInterface, i ->

//                해당 답글 삭제 -> API 요청 + 새로고침

                    apiService.getRequestAppointmentDelete(data.id).enqueue(object :
                        Callback<BasicResponse> {
                        override fun onResponse(
                            call: Call<BasicResponse>,
                            response: Response<BasicResponse>
                        ) {
                            if (response.isSuccessful) {
                                Toast.makeText(mContext, "약속이 삭제되었습니다", Toast.LENGTH_SHORT).show()
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
        return AppointmentViewHolder(mContext, view)
    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        val data = mList[position]
        holder.bind(mContext, data)
    }

    override fun getItemCount(): Int {
        return mList.size
    }


}