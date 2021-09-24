package com.example.databinding20210910.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.databinding20210910.R
import com.example.databinding20210910.ViewAppointmentDetailActivity
import com.example.databinding20210910.ViewMapActivity
import com.example.databinding20210910.datas.AppointmentData

class InvitedAppointmentRecyclerAdapter(
    val mContext: Context,
    val mList: List<AppointmentData>) :
    RecyclerView.Adapter<InvitedAppointmentRecyclerAdapter.AppointmentViewHolder>() {

    class AppointmentViewHolder(val mContext: Context,view : View) : RecyclerView.ViewHolder(view) {

        val titleTxt = view.findViewById<TextView>(R.id.titleTxt)
        val dateTimeTxt = view.findViewById<TextView>(R.id.dateTimeTxt)
        val placeNameTxt = view.findViewById<TextView>(R.id.placeNameTxt)
        val viewPlaceMapBtn = view.findViewById<ImageView>(R.id.viewPlaceMapBtn)
        val rootLayout = view.findViewById<LinearLayout>(R.id.rootLayout)
        val inviteUserProfile = view.findViewById<ImageView>(R.id.inviteUserProfile)
        val inviteUserName = view.findViewById<TextView>(R.id.inviteUserName)


        fun bind( context: Context, data: AppointmentData ) {
            titleTxt.text = data.title

//            약속 일시를 : Date형태로 파싱됨 -> String으로 가공해야 함 -> SimpleDateFormat을 사용해야 함
            dateTimeTxt.text = data.getFormattedDateTime()
            placeNameTxt.text = data.placeName


            inviteUserName.text = data.user.nickName
            Glide.with(context).load(data.user.profileImgURL).into(inviteUserProfile)

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

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.invited_appointment_list_item, parent, false)

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