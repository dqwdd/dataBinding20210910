package com.example.databinding20210910.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.databinding20210910.AddFriendActivity
import com.example.databinding20210910.R
import com.example.databinding20210910.ViewMyFriendsListActivity
import com.example.databinding20210910.datas.BasicResponse
import com.example.databinding20210910.datas.UserData
import com.example.databinding20210910.fragments.RequestedUserListFragment
import com.example.databinding20210910.web.ServerAPI
import com.example.databinding20210910.web.ServerAPIService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RequestUserRecyclerAdapter(
    val mContext: Context,
    val mList: List<UserData> ) : RecyclerView.Adapter<RequestUserRecyclerAdapter.AddFriendViewHolder>() {

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


            //수락 거절 둘 다 하는 일은 같지만 파라미터만 다르다
            //버튼에 미리 태그를 달아두고 꺼내서 쓰는 동일한 작업



            val sendOrNoToserver = object : View.OnClickListener {
                override fun onClick(p0: View?) {

                    val okOrNo = p0!!.tag.toString()
                    Log.d("서버로 보낼 값이 뭐냐 : ", okOrNo)

//                    어댑터에서 API 서비스 사용법
//                    1) 직접 만들자 (이 어댑터에서 사용할 방법)
//                    2) 화면의 (context) 의 변수를 활용 (액티비티의 어댑터에서 활용이 편함)

                    val apiService = ServerAPI.getRetrofit(context).create(ServerAPIService::class.java)

                    apiService.putRequestRequestReply(data.id, okOrNo).enqueue(object : Callback<BasicResponse> {
                        override fun onResponse(
                            call: Call<BasicResponse>,
                            response: Response<BasicResponse>
                        ) {
                            //프래그먼트의 요청목록 새로 받아오기 함수를 실행
                            //어댑터에서 -> 액티비티 기능 : context 변수 활용

                            //어댑터 -> 액티비티 -> ViewPager 어댑터 -> 1번째 프레그먼트 ->요청 목록 Frag로 변신 -> 기능 활용
                            ((context as ViewMyFriendsListActivity)
                                .mFPA.getItem(1) as RequestedUserListFragment)
                                .getRequestUserListFromServer()

                        }

                        override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                        }
                    })


                }
            }

            agreeBtn.setOnClickListener (sendOrNoToserver)
            refuseBtn.setOnClickListener (sendOrNoToserver)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddFriendViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.requested_user_list_item, parent, false)
        return AddFriendViewHolder(view)
    }

    override fun onBindViewHolder(holder: AddFriendViewHolder, position: Int) {
        holder.bind(mContext, mList[position])
    }

    override fun getItemCount() = mList.size
}