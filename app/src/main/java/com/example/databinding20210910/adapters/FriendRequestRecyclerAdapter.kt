package com.example.databinding20210910.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.databinding20210910.R
import com.example.databinding20210910.datas.UserData

class FriendRequestRecyclerAdapter(
    val mContext: Context,
    val mList: List<UserData> ) : RecyclerView.Adapter<FriendRequestRecyclerAdapter.AddFriendViewHolder>() {

    class AddFriendViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val friendProfileImg = view.findViewById<ImageView>(R.id.friendProfileImg)
        val nicknameTxt = view.findViewById<TextView>(R.id.nicknameTxt)
        val socialLoginImg = view.findViewById<ImageView>(R.id.socialLoginImg)
        val agreeBtn = view.findViewById<Button>(R.id.agreeBtn)
        val refuseBtn = view.findViewById<Button>(R.id.refuseBtn)

        fun bind(context: Context, data: UserData) {

            nicknameTxt.text = data.nickName

            Glide.with(context).load(data.profileImgURL).into(friendProfileImg)

            when (data.provider){
                "facebook" -> {
                    socialLoginImg.setImageResource(R.drawable.facebook_logo_icon)
                    socialLoginImg.visibility = View.VISIBLE
                }
                "kakao" -> {
                    socialLoginImg.setImageResource(R.drawable.kakaotalk_logo_icon)
                    socialLoginImg.visibility = View.VISIBLE
                }
                else -> {
                    socialLoginImg.visibility = View.VISIBLE
                }
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddFriendViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.requested_user_list_item, parent, false)
        return AddFriendViewHolder(view)
    }

    override fun onBindViewHolder(holder: AddFriendViewHolder, position: Int) {
        val data = mList[position]
        holder.bind(mContext, data)
    }

    override fun getItemCount() = mList.size
}