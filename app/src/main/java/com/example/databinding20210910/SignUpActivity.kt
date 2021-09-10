package com.example.databinding20210910

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.example.databinding20210910.databinding.ActivitySignUpBinding
import com.example.databinding20210910.datas.BasicResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : BaseActivity() {

    lateinit var binding : ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        setupEvent()
        setValues()
    }

    override fun setupEvent() {

        binding.sighUpBtn.setOnClickListener {

            val inputEmail = binding.emailEdt.text.toString()
            val inputPw = binding.pwEdt.text.toString()
            val inputNick = binding.nicknameEdt.text.toString()

            apiService.putRequestSignUp(inputEmail, inputPw, inputNick).enqueue(object : Callback<BasicResponse> {
                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {

                    val basicResponse = response.body()!!

                    Log.d("서버 메시지", basicResponse.message)

                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                }
            })
            //여기까지가 요청하는 기능은 끝, 사잏업 해주세요~

        }

    }

    override fun setValues() {
    }
}