package com.appointments.databinding20211110.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.appointments.databinding20211110.R
import com.appointments.databinding20211110.ViewMyFriendsListActivity
import com.appointments.databinding20211110.datas.BasicResponse
import com.appointments.databinding20211110.datas.UserData
import com.appointments.databinding20211110.fragments.MyFriendsListFragment
import com.appointments.databinding20211110.web.ServerAPI
import com.appointments.databinding20211110.web.ServerAPIService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyFriendsRecyclerAdapter(
    val mContext: Context,
    val mList: List<UserData> ) : RecyclerView.Adapter<MyFriendsRecyclerAdapter.FriendViewHolder>() {

    inner class FriendViewHolder(view : View) : BaseViewHolder(mContext, view) {
        val friendProfileImg = view.findViewById<ImageView>(R.id.friendProfileImg)
        val nicknameTxt = view.findViewById<TextView>(R.id.nicknameTxt)
        val socialLoginImg = view.findViewById<ImageView>(R.id.socialLoginImg)
        val friendLinearLayout = view.findViewById<LinearLayout>(R.id.friendLinearLayout)

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
                "naver" -> {
                    socialLoginImg.setImageResource(R.drawable.naver_icon)
                    socialLoginImg.visibility = View.VISIBLE
                }
                else -> {
                    socialLoginImg.visibility = View.VISIBLE
                }
            }



            friendLinearLayout.setOnLongClickListener {

                val alert = AlertDialog.Builder(mContext)
                alert.setMessage("?????? ${data.nickName}?????? ?????????????????????????")
                val DeleteNick = data.nickName
                alert.setNegativeButton("??????", DialogInterface.OnClickListener { dialogInterface, i ->

//                ?????? ?????? ?????? -> API ?????? + ????????????
                    val apiService = ServerAPI.getRetrofit(mContext).create(ServerAPIService::class.java)

                    apiService.getRequestFriendDelete(data.id).enqueue(object :
                        Callback<BasicResponse> {
                        override fun onResponse(
                            call: Call<BasicResponse>,
                            response: Response<BasicResponse>
                        ) {
                            Toast.makeText(mContext, DeleteNick + "?????? ?????????????????????", Toast.LENGTH_SHORT).show()
                            ((context as ViewMyFriendsListActivity)
                                .mFPA.getItem(0) as MyFriendsListFragment)
                                .getMyFriendListFromServer()
                        }

                        override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                        }
                    })
                })
                alert.setPositiveButton("??????", null)
                alert.show()

                return@setOnLongClickListener true
            }


        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.friend_list_item, parent, false)
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