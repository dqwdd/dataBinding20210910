package com.example.databinding20210910.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.databinding20210910.R
import com.example.databinding20210910.datas.DataResponse
import com.example.databinding20210910.datas.NotificationData
import com.example.databinding20210910.datas.UserData
import com.gun0912.tedpermission.provider.TedPermissionProvider.context

class NotificationRecyclerAdapter(
    val mContext: Context,
    val mList: List<NotificationData> ) : RecyclerView.Adapter<NotificationRecyclerAdapter.FriendViewHolder>() {

    class FriendViewHolder(view : View) : RecyclerView.ViewHolder(view) {

        val noticeTxt = view.findViewById<TextView>(R.id.noticeTxt)

        fun bind(context: Context, data: NotificationData) {
            //noticeTxt.text = data.notifications.toString()
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