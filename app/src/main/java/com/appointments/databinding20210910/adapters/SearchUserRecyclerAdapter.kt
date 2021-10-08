package com.appointments.databinding20210910.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.appointments.databinding20210910.AddFriendActivity
import com.appointments.databinding20210910.R
import com.appointments.databinding20210910.datas.BasicResponse
import com.appointments.databinding20210910.datas.UserData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchUserRecyclerAdapter(

    val mContext: Context,
    val mList: List<UserData>) : RecyclerView.Adapter<SearchUserRecyclerAdapter.UserViewHolder>() {

    inner class UserViewHolder(view : View) : BaseViewHolder(mContext, view) {

        val ProfileImg = view.findViewById<ImageView>(R.id.friendProfileImg)
        val nicknameTxt = view.findViewById<TextView>(R.id.nicknameTxt)
        val socialLoginImg = view.findViewById<ImageView>(R.id.socialLoginImg)
        val addFriendBtn = view.findViewById<Button>(R.id.addFriendBtn)


        fun bind(context: Context, data: UserData) {

            nicknameTxt.text = data.nickName
            Glide.with(context).load(data.profileImgURL).into(ProfileImg)
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

//            친구 추가 버튼 눌림 이벤트 처리
            addFriendBtn.setOnClickListener {
//                ~~님을 친구로 추가하시겠습니까?

                val alert = AlertDialog.Builder(context)
                alert.setMessage("${data.nickName}님에게 친구 요청을 보내시겠습니까?")
                alert.setNegativeButton("확인" , DialogInterface.OnClickListener { dialogInterface, i ->

                    (context as AddFriendActivity).apiService.postRequestAddFriend(data.id).enqueue(object :
                        Callback<BasicResponse> {
                        override fun onResponse(
                            call: Call<BasicResponse>,
                            response: Response<BasicResponse>
                        ) {
                            if (response.isSuccessful) {
                                Toast.makeText(context, "${data.nickName}님에게 친구 요청을 보냈습니다", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                        }
                    })

                })
                alert.setPositiveButton("취소", null)
                alert.show()
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.search_user_list_item, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val data = mList[position]
        holder.bind(mContext, data)
    }

    override fun getItemCount() = mList.size

}