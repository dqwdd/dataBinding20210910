package com.example.databinding20210910

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.example.databinding20210910.databinding.ActivityMySettinsgBinding
import com.example.databinding20210910.datas.BasicResponse
import com.example.databinding20210910.utils.GlobalData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MySettinsgActivity : BaseActivity() {

    lateinit var binding : ActivityMySettinsgBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_settinsg)
        setupEvent()
        setValues()
    }

    override fun setupEvent() {


        binding.editNicknameLayout.setOnClickListener {

            //응용문제 => AlertDialog로 닉네임을 입력받자
            //editText를 사용할 수 있는 방법2

            //PATCH - /user => field : nickname으로 보내서 닉변

            val customView = LayoutInflater.from(mContext).inflate(R.layout.my_custom_nickname_alert, null)
            val alert = AlertDialog.Builder(mContext)

            alert.setTitle("닉네임 변경")
            alert.setView(customView)
            alert.setPositiveButton("확인", DialogInterface.OnClickListener { dialogInterface, i ->
                val nicknameEdt = customView.findViewById<EditText>(R.id.nicknameEdt)

                apiService.patchRequestMyInfo("nickname", nicknameEdt.text.toString()).
                enqueue(object : Callback<BasicResponse> {
                    override fun onResponse(
                        call: Call<BasicResponse>,
                        response: Response<BasicResponse>
                    ) {
                        if (response.isSuccessful) {

                            val basicResponse = response.body()!!
                            GlobalData.loginUser = basicResponse.data.user
                            setUserInfo()

                        }
                    }

                    override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                    }
                })

            })

            alert.setNegativeButton("취소", null)
            alert.show()

        }

        binding.readyTimeLayout.setOnClickListener {

            //응용문제 => AlertDialog로 준비시간을 입력받자
            //EditText를 사용할 수 있는 방법?

            val customView = LayoutInflater.from(mContext).inflate(R.layout.my_custom_alert_edt, null)

            val alert = AlertDialog.Builder(mContext)

            alert.setTitle("준비시간결정")
            //커스텀뷰를 가져와서 alert의 view로 설정
            alert.setView(customView)
            alert.setPositiveButton("확인", DialogInterface.OnClickListener { dialogInterface, i ->

                val minuteEdt = customView.findViewById<EditText>(R.id.minuteEdt)

                //Toast.makeText(mContext, "${minuteEdt.text.toString()}", Toast.LENGTH_SHORT).show()

                //enqueue==갔다와서 뭐할건지
                apiService.patchRequestMyInfo("ready_minute", minuteEdt.text.toString()).
                enqueue(object : Callback<BasicResponse> {
                    override fun onResponse(
                        call: Call<BasicResponse>,
                        response: Response<BasicResponse>
                    ) {
                        if (response.isSuccessful) {

                            val basicResponse = response.body()!!
                            GlobalData.loginUser = basicResponse.data.user
                            setUserInfo()

                        }
                    }

                    override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                    }
                })

            })

            alert.setNegativeButton("취소", null)
            alert.show()

        }

    }

    override fun setValues() {
        titleTxt.text = "프로필설정"
        setUserInfo()

        binding.nicknameTxt.text = GlobalData.loginUser!!.nickName

        //로그인 한 사람의 준비시간이 1시간 이상이냐 아니냐
        if (GlobalData.loginUser!!.readyMinute >= 60) {
            val hour = GlobalData.loginUser!!.readyMinute / 60
            val minute = GlobalData.loginUser!!.readyMinute % 60

            binding.readyTimeTxt.text = "${hour}시간 ${minute}분"
        }
        else {
            binding.readyTimeTxt.text = "${GlobalData.loginUser!!.readyMinute}분"
        }

        when(GlobalData.loginUser!!.provider) {
            "facebook" -> binding.socialLoginImg.setImageResource(R.drawable.facebook_logo_icon)
            "kakao" -> binding.socialLoginImg.setImageResource(R.drawable.kakaotalk_logo_icon)
            else -> binding.socialLoginImg.visibility = View.VISIBLE
        }

        /*if (GlobalData.loginUser!!.provider == "facebook") {
            binding.socialLoginImg.setImageResource(R.drawable.facebook_logo_icon)
        }
        else if (GlobalData.loginUser!!.provider == "kakao") {
            binding.socialLoginImg.setImageResource(R.drawable.kakaotalk_logo_icon)
        }*/
        //코틀린스럽지 않아서 위처럼 바꾸심

        //일반로그인일때는 비번 변경 UI 표시
        when (GlobalData.loginUser!!.provider) {
            "default" -> binding.passwordLayout.visibility = View.VISIBLE
            else -> binding.passwordLayout.visibility = View.GONE
        }

    }


    fun setUserInfo() {

    }

}