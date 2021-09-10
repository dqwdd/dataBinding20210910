package com.example.databinding20210910

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.databinding20210910.databinding.ActivitySignUpBinding
import com.example.databinding20210910.datas.BasicResponse
import org.json.JSONObject
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

//                    response.body => 응답 코드가 200번이어야 들어있다
//                    가입 실패 / 로그인 실패 => 응답 코드 400-> errorBody에서 따로 찾아야 함(실패)
//                    경우마다 해서 해야 함(if문)

                    if( response.isSuccessful ) {

                        val basicResponse = response.body()!!
                        Log.d("서버 메시지", basicResponse.message)
                        Toast.makeText(mContext, basicResponse.message, Toast.LENGTH_SHORT).show()

                    }
                    else {
//                        어떤 이유건 성공이 아닌 상황
                        val errorBodyStr = response.errorBody()!!.string()

//                        단순 JSON 형태의 String으로 내려옴 => JSONObject 형태로 가공
                        Log.d("에러인 경우의 데이터 : ", errorBodyStr)
                        val jsonObj = JSONObject(errorBodyStr)
                        val message = jsonObj.getString("message")

//                        runOnUiThread를 해주지 않아도 UI 접근 가능
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()

                    }



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