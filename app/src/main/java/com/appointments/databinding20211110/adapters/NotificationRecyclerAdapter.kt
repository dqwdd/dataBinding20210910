package com.appointments.databinding20211110.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.appointments.databinding20211110.R
import com.appointments.databinding20211110.datas.NotificationData

class NotificationRecyclerAdapter(
    val mContext: Context,
    val mList: List<NotificationData> ) : RecyclerView.Adapter<NotificationRecyclerAdapter.FriendViewHolder>() {

    inner class FriendViewHolder(view : View) : BaseViewHolder(mContext, view) {

        val noticeActUserTxt = view.findViewById<TextView>(R.id.noticeActUserTxt)
        val actUserProfileImg = view.findViewById<ImageView>(R.id.actUserProfileImg)
        val noticeMessageTxt = view.findViewById<TextView>(R.id.noticeMessageTxt)
        val noticeTitle = view.findViewById<TextView>(R.id.noticeTitle)

        fun bind(context: Context, data: NotificationData) {
            noticeTitle.text = data.title
            Glide.with(context).load(data.act_user.profile_img).into(actUserProfileImg)
            noticeActUserTxt.text = data.act_user.nick_name
            noticeMessageTxt.text = data.message
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.notifications_list_item, parent, false)
        return FriendViewHolder(view)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {

        val data = mList[position]
        holder.bind(mContext, data)

    }

    override fun getItemCount(): Int {
        return mList.size
    }


}