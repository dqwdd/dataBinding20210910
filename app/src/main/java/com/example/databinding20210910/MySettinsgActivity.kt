package com.example.databinding20210910

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
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

    }


    fun setUserInfo() {

    }

}