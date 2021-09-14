package com.example.databinding20210910

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.databinding20210910.databinding.ActivityMySettinsgBinding
import com.example.databinding20210910.utils.GlobalData

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

        }

    }

    override fun setValues() {
        titleTxt.text = "프로필설정"

        binding.nicknameTxt.text = GlobalData.loginUser!!.nickName

        //로그인 한 사람의 준비시간이 1시간 이상이냐 아니냐
        if (GlobalData.loginUser!!.readyMinute >= 60) {
            val hour = GlobalData.loginUser!!.readyMinute / 60
            val minute = GlobalData.loginUser!!.readyMinute % 60

            binding.readyTimeTxt.text = "${hour}시간 ${minute}분"
        }
        else {
            binding.readyTimeTxt.text = "${GlobalData.loginUser!!}시간 ${minute}분"
        }

    }
}