package com.example.databinding20210910

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.example.databinding20210910.databinding.ActivityLoginBinding
import java.security.MessageDigest

import com.facebook.login.LoginResult

import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.example.databinding20210910.datas.BasicResponse
import com.example.databinding20210910.datas.UserData
import com.example.databinding20210910.utils.ContextUtil
import com.example.databinding20210910.utils.GlobalData
import com.facebook.*
import com.facebook.login.LoginManager

import com.facebook.login.widget.LoginButton
import com.kakao.sdk.auth.model.OAuthToken
import org.json.JSONObject
import java.util.*

import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLoginHandler
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : BaseActivity() {

    lateinit var binding : ActivityLoginBinding

    lateinit var callbackManager: CallbackManager


    lateinit var mNaverLoginModule: OAuthLogin

//    var keyHash = Utility.getKeyHash(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        setupEvent()
        setValues()

    }

    override fun setupEvent() {


        binding.naverLoginBtn.setOnClickListener {

            mNaverLoginModule.startOauthLoginActivity(this, object : OAuthLoginHandler(){
                override fun run(success: Boolean) {

                    if (success) {

                        //네이버 로그인 성공하면 그 계정의 토큰 값 추출
                        val accessToken = mNaverLoginModule.getAccessToken(mContext)
                        Log.d("네이버 토큰 값", accessToken)

                        //별개의 통신용 스레드 생성 -> 내 정보 요청
                        Thread {
//                            이 내부의 코드를 백그라운드 실행
                            val url = "https://openapi.naver.com/v1/nid/me"
                            val jsonObj = JSONObject(mNaverLoginModule.requestApi(mContext, accessToken, url))
                            Log.d("네이버 로그인 내 정보", jsonObj.toString())

                            val responseObj = jsonObj.getJSONObject("response")

                            //정보 추출
                            val uid = responseObj.getString("id")
                            val name = responseObj.getString("name")

                            //우리 서버로 전달
                            apiService.postRequestSocialLogin("naver", uid, name)
                                .enqueue(object : retrofit2.Callback<BasicResponse> {
                                    override fun onResponse(
                                        call: Call<BasicResponse>,
                                        response: Response<BasicResponse>
                                    ) {

                                        //소셜 로그인 마무리 -> 토큰, GlobalData사용자 -> 메인으로 이동

                                        val basicResponse = response.body()!!
                                        Toast.makeText(mContext, "${name}님 환영합니다", Toast.LENGTH_SHORT).show()

                                        Log.d("API서버가 준 토큰값 : ", basicResponse.data.token)
                                        ContextUtil.setToken(mContext,basicResponse.data.token)

                                        GlobalData.loginUser = basicResponse.data.user
                                        moveToMain()

                                    }

                                    override fun onFailure(
                                        call: Call<BasicResponse>,
                                        t: Throwable
                                    ) {
                                    }
                                })

                        }.start()



                    }
                    else {
                        Toast.makeText(mContext, "네이버 로그인에 실패했습니다", Toast.LENGTH_SHORT).show()
                    }

                }
            })

        }



        binding.loginBtn.setOnClickListener {

            val inputEmail = binding.emailEdt.text.toString()
            val inputPw = binding.pwEdt.text.toString()

            apiService.putRequestSignIn(inputEmail, inputPw).enqueue(object :
                Callback<BasicResponse> {
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
//                        Toast.makeText(mContext, basicResponse.message, Toast.LENGTH_SHORT).show()
                        Toast.makeText(mContext, "${basicResponse.data.user.nickName}님 환영합니다", Toast.LENGTH_SHORT).show()


//                        로그인 성공 =? "data" jsonObject -> DataResponse -> token변수.
                        Log.d("토큰", basicResponse.data.token)
                        ContextUtil.setToken(mContext,basicResponse.data.token)

//                        Toast.makeText(mContext, basicResponse.data.user.email, Toast.LENGTH_SHORT).show()

//                        로그인 한 사람이 누구인지 GlobalData에 저장하고 싶다->
                        GlobalData.loginUser = basicResponse.data.user

                        moveToMain()

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


        binding.sighUpBtn.setOnClickListener {
            val myIntent = Intent(mContext, SignUpActivity::class.java)
            startActivity(myIntent)
        }


        binding.kakaoLoginBtn.setOnClickListener {
            // 카카오계정으로 로그인
            UserApiClient.instance.loginWithKakaoAccount(mContext) { token, error ->
                if (error != null) {
                    Log.e("카카오로그인", "로그인 실패", error)
                }
                else if (token != null) {
                    Log.i("카카오로그인", "로그인 성공 ${token.accessToken}")

                    UserApiClient.instance.me { user, error ->
                        if (error != null) {
                            Log.e("카카오로그인", "사용자 정보 요청 실패", error)
                        }
                        else if (user != null) {
                            Log.i("카카오로그인", "사용자 정보 요청 성공" +
                                    "\n회원번호: ${user.id}" +
                                    "\n이메일: ${user.kakaoAccount?.email}" +
                                    "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                                    "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}"
                            )



//                            소셜로그인 API에 "kakao" 로 id / 닉네임 전송. (도전과제)

                            val id = user.id.toString()
                            val name = user.kakaoAccount?.profile?.nickname.toString()

                            apiService.postRequestSocialLogin("kakao", id, name).enqueue(object : Callback<BasicResponse> {
                                override fun onResponse(
                                    call: Call<BasicResponse>,
                                    response: Response<BasicResponse>
                                ) {
                                    val basicResponse = response.body()!!
//                                    Toast.makeText(mContext, basicResponse.message, Toast.LENGTH_SHORT).show()
                                    Toast.makeText(mContext, "${name}님 환영합니다", Toast.LENGTH_SHORT).show()

//                                    ContextUtil 등으로 SharedPreferences로 토큰값 저장
                                    Log.d("API서버가 준 토큰값 : ", basicResponse.data.token)
                                    ContextUtil.setToken(mContext,basicResponse.data.token)

//                        로그인 한 사람이 누구인지 GlobalData에 저장하고 싶다->
                                    GlobalData.loginUser = basicResponse.data.user
                                    moveToMain()

                                }

                                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                                }
                            })



                        }
                    }

                }
            }//이제부턴
        }




        binding.facebookLoginBtn.setOnClickListener {

//            우리가 붙인 버튼에 기능 활용

//            커스텀 버튼에 로그인 하고 돌아온 callback을 따로 설정

            LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {

                    Log.d("로그인 성공", "우리가 만든 버튼으로 성공")

//                    페이스북에 접근할 수 있는 토큰이 생겨있다 -> 활용
//                    나의 (로그인한 사람의) 정보(GraphRequest)를 받아오는데 활용

                    //object == 갔다 와서 뭐 할건지 적는거
                    val graphRequest = GraphRequest.newMeRequest(result?.accessToken, object : GraphRequest.GraphJSONObjectCallback {
                        override fun onCompleted(jsonObj: JSONObject?, response: GraphResponse?) {

                            Log.d("내 정보 내용", jsonObj.toString())

                            val name = jsonObj!!.getString("name")
                            val id = jsonObj.getString("id")

//                            가입한 회원 이름 => 우리 서버에 사용자 이름으로 (닉네임으로) 저장
                            Log.d("이름", name)
//                            페북으로 사용자를 구별하는 고유번호 -> 우리 서버에 같이 저장. 회원가입 or 로그인 근거자료로 활용
                            Log.d("아이디", id)

//                            TODO() - 페북이 알려준 이름/id값을 API서버에 전달해서 소셜로그인 처리 요청

                            apiService.postRequestSocialLogin("facebook", id, name).enqueue(object : Callback<BasicResponse> {
                                override fun onResponse(
                                    call: Call<BasicResponse>,
                                    response: Response<BasicResponse>
                                ) {

                                    val basicResponse = response.body()!!
//                                    Toast.makeText(mContext, basicResponse.message, Toast.LENGTH_SHORT).show()
                                    Toast.makeText(mContext, "${name}님 환영합니다", Toast.LENGTH_SHORT).show()
                                    Log.d("API서버가 준 토큰값 : ", basicResponse.data.token)

//                                    ContextUtil 등으로 SharedPreferences로 토큰값 저장
                                    ContextUtil.setToken(mContext,basicResponse.data.token)

//                        로그인 한 사람이 누구인지 GlobalData에 저장하고 싶다->
                                    GlobalData.loginUser = basicResponse.data.user
                                    moveToMain()


//                                    메인화면으로 이동
//                                    val myIntent = Intent(mContext, MainActivity::class.java)
//                                    startActivity(myIntent)
//                                    finish()

                                }

                                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                                }
                            })

                        }
                    })

//                    위에서 정리한 내용을 들고, 내 정보를 요청
                    graphRequest.executeAsync()

                }

                override fun onCancel() {
                }

                override fun onError(error: FacebookException?) {
                }

            })


            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
        }

        // Callback registration

        // Callback registration
//        binding.loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
//            override fun onSuccess(loginResult: LoginResult?) {
//                // App code
//                Log.d("확인용", loginResult.toString())
//
//                val accessToken = AccessToken.getCurrentAccessToken()
//                Log.d("페북토큰", accessToken.toString())
//            }
//
//            override fun onCancel() {
//                // App code
//            }
//
//            override fun onError(exception: FacebookException) {
//                // App code
//            }
//        })

    }

    override fun setValues() {


        //페북 로그인 콜백 관련 함수
        callbackManager = CallbackManager.Factory.create();


        //네이버 로그인 모듈 세팅
        mNaverLoginModule = OAuthLogin.getInstance()
        mNaverLoginModule.init(mContext,
            resources.getString(R.string.naver_client_id),
            resources.getString(R.string.naver_secret_key),
            resources.getString(R.string.naver_client_name)
        )





        logoImg.visibility = View.VISIBLE
        titleTxt.visibility = View.GONE

//        카톡으로 받은 코드 복붙 => 키 해시값 추출

        val info = packageManager.getPackageInfo(
            "com.example.databinding20210910",
            PackageManager.GET_SIGNATURES
        )
        for (signature in info.signatures) {
            val md: MessageDigest = MessageDigest.getInstance("SHA")
            md.update(signature.toByteArray())
            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        Log.d("확인용", "확인")
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun moveToMain() {
        val myIntent = Intent(mContext, MainActivity::class.java)
        startActivity(myIntent)
        finish()
    }

}