package com.appointment.databinding20210910

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.appointment.databinding20210910.databinding.ActivitySignUpBinding
import com.appointment.databinding20210910.datas.BasicResponse
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

        binding.emailCheckBtn.setOnClickListener {

            val inputEmail = binding.emailEdt.text.toString()
            val inputPw = binding.pwEdt.text.toString()
            val inputRepeatPw = binding.pwRepeatEdt.text.toString()
            val inputNick = binding.nicknameEdt.text.toString()

            if (inputEmail == "") {
                binding.emailTxt.text = "아이디를 입력해 주세요"
            }

            else {
                fun String.isEmailValid(): Boolean {
                    return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
                }
                inputEmail.isEmailValid()

                //val emailValidation = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
                //val p = Pattern.matches(emailValidation, inputEmail)

                if (inputEmail.isEmailValid()) {
                    apiService.getRequestIDNicknameCheck("EMAIL", inputEmail)
                        .enqueue(object : Callback<BasicResponse> {
                            override fun onResponse(
                                call: Call<BasicResponse>,
                                response: Response<BasicResponse>
                            ) {
                                if (response.isSuccessful) {
                                    binding.emailTxt.text = "사용 가능한 이메일입니다"
                                }
                                else {
                                    val errorBodyStr = response.errorBody()!!.string()
                                    val jsonObj = JSONObject(errorBodyStr)
                                    val message = jsonObj.getString("message")
                                    binding.emailTxt.text = message
                                }
                            }

                            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                            }
                        })
                }
                else {
                    binding.emailTxt.text = "아이디를 이메일 양식으로 입력해 주세요"
                }
            }

        }


        binding.nicknameCheckBtn.setOnClickListener {

            val inputEmail = binding.emailEdt.text.toString()
            val inputPw = binding.pwEdt.text.toString()
            val inputRepeatPw = binding.pwRepeatEdt.text.toString()
            val inputNick = binding.nicknameEdt.text.toString()


            if (inputNick == "") {
                binding.nicknameTxt.text = "닉네임을 입력해 주세요"
            }

            else {
                apiService.getRequestIDNicknameCheck("NICK_NAME", inputNick)
                    .enqueue(object : Callback<BasicResponse> {
                        override fun onResponse(
                            call: Call<BasicResponse>,
                            response: Response<BasicResponse>
                        ) {
                            if (response.isSuccessful) {
                                binding.nicknameTxt.text = "사용 가능한 닉네임입니다"
                            }
                            else {
                                val errorBodyStr = response.errorBody()!!.string()
                                val jsonObj = JSONObject(errorBodyStr)
                                val message = jsonObj.getString("message")

                                binding.nicknameTxt.text = message
                            }
                        }

                        override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                        }
                    })
            }

        }

        binding.sighUpBtn.setOnClickListener {

            val inputEmail = binding.emailEdt.text.toString()
            val inputPw = binding.pwEdt.text.toString()
            val inputRepeatPw = binding.pwRepeatEdt.text.toString()
            val inputNick = binding.nicknameEdt.text.toString()



            if (binding.emailTxt.text != "사용 가능한 이메일입니다") {
                binding.emailTxt.text = "아이디를 확인해 주세요"
            }

            else if (inputPw != inputRepeatPw) {
                binding.checkTxt.text = "같은 비밀번호를 입력해 주세요"
            }

            else if (binding.nicknameTxt.text != "사용 가능한 닉네임입니다") {
                binding.nicknameTxt.text = "닉네임을 확인해 주세요"
            }

            else {
                apiService.putRequestSignUp(inputEmail, inputPw, inputNick)
                    .enqueue(object : Callback<BasicResponse> {
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
                            finish()
                        }
                    }
                    override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                    }
                })
                //여기까지가 요청하는 기능은 끝, 사잏업 해주세요~
            }






        }
    }

    override fun setValues() {
    }
}