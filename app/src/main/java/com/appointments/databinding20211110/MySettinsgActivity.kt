package com.appointments.databinding20211110

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.appointments.databinding20211110.databinding.ActivityMySettinsgBinding
import com.appointments.databinding20211110.datas.BasicResponse
import com.appointments.databinding20211110.utils.ContextUtil
import com.appointments.databinding20211110.utils.GlobalData
import com.appointments.databinding20211110.utils.URIPathHelper
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class MySettinsgActivity : BaseActivity() {

    lateinit var binding : ActivityMySettinsgBinding

    //프사 가지러 갤러리로 이동
    val REQ_FOR_GALLERY = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_settinsg)
        setupEvent()
        setValues()
    }



    override fun setupEvent() {


        //회원 탈퇴
        binding.unregisterBtn.setOnClickListener {

            val customView = LayoutInflater.from(mContext).inflate(R.layout.my_custom_unregister_alert, null)
            val alert = AlertDialog.Builder(mContext)

            alert.setTitle("회원 탈퇴")
            alert.setView(customView)
            alert.setNegativeButton("확인", DialogInterface.OnClickListener { dialogInterface, i ->
                val confirmEdt = customView.findViewById<EditText>(R.id.confirmEdt)

                apiService.getRequestUserDelete(confirmEdt.text.toString()).enqueue(object : Callback<BasicResponse> {
                    override fun onResponse(
                        call: Call<BasicResponse>,
                        response: Response<BasicResponse>
                    ) {
                        if (response.isSuccessful) {

                            Toast.makeText(mContext, "회원탈퇴가 되었습니다!", Toast.LENGTH_SHORT).show()

                            val myIntent = Intent(mContext, LoginActivity::class.java)
                            startActivity(myIntent)
                        }
                    }

                    override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                    }
                })

            })

            alert.setPositiveButton("취소", null)
            alert.show()

        }


        //프로필 삭제
        binding.ProfileRefreshIcon.setOnClickListener {
            val alert = AlertDialog.Builder(mContext)
            alert.setMessage("프로필을 삭제 하시겠습니까?")
            alert.setNegativeButton("확인", DialogInterface.OnClickListener { dialogInterface, i ->

                apiService.getRequestProfileImageDelete().enqueue(object : Callback<BasicResponse> {
                    override fun onResponse(
                        call: Call<BasicResponse>,
                        response: Response<BasicResponse>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(mContext, "프로필이 삭제되었습니다", Toast.LENGTH_SHORT).show()

                            //

                        }


                    }

                    override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                    }
                })

            })
            alert.setPositiveButton("취소", null)
            alert.show()


        }


        //비밀번호 변경
        binding.passwordLayout.setOnClickListener {
            val myIntent = Intent(mContext, EditPasswordActivity::class.java)
            startActivity(myIntent)
        }


        //로그아웃
        binding.logoutImg.setOnClickListener {
            val alert = AlertDialog.Builder(mContext)
            alert.setMessage("정말 로그아웃 하시겠습니까?")
            alert.setNegativeButton("확인", DialogInterface.OnClickListener { dialogInterface, i ->

//             => token 없던걸로. => ContextUtil에 저장된 토큰값을 "" 으로 바꿔주자.
                ContextUtil.setToken(mContext, "")

//             => GlobalaData.loginUser 없던걸로. -> SplashActivity에서 Handler쪽 보면 로그인유저 관련된 함수 있음
                GlobalData.loginUser = null

//             => 모두 끝나면 모든 화면 종료, 로그인 화면으로 이동.

                val myIntent = Intent(mContext, LoginActivity::class.java)
                myIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(myIntent)
//                finish() => MyProfile화면 하나만 종료.
//                Intent 부가기능 활용 -> flag로 기존화면 (열었던 모든화면) 전부 종료
            })
            alert.setPositiveButton("취소", null)
            alert.show()
        }




        //프로필 사진 누르면 => 프사 변경 =-> 갤러릴로 프사 선택하러 진입/
        //안드로이드가 제공하는 갤러리 화면 활용(trello의 Intent(4) 추가 항목)
        //어떤 사진? 결과를 얻기 위해 화면 이동(trello의 Intent(3)의 활용)
        binding.profileImg.setOnClickListener {

            val alert = AlertDialog.Builder(mContext)
            alert.setMessage("프로필 이미지를 변경하시겠습니까?")
            alert.setNegativeButton("확인", DialogInterface.OnClickListener { dialogInterface, i ->


                //갤러리를 개발자가 이용 -> 퍼미션 받아야 함 -> 권한 세팅 필요
                //TedPermission 라이브러리

                val permissionListener = object : PermissionListener {
                    override fun onPermissionGranted() {
                        //권한이 ok일 때 -> 갤러리로 사진 가지러 이동(추가 작업)

                        val myIntent = Intent()
                        myIntent.action = Intent.ACTION_PICK // 겟하러 간다
                        myIntent.type = android.provider.MediaStore.Images.Media.CONTENT_TYPE
                        startActivityForResult(myIntent, REQ_FOR_GALLERY)
                        //'(myIntent, "프사 선택하기"),' 이후 == "프사선택하기"가 뭘 가지러 가나

                    }

                    override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                        //(최종으로)권한이 거절당했을 때 => 토스트로 안내만 하자
                        Toast.makeText(mContext, "권한이 거부되어 갤러리에 접근이 불가능합니다", Toast.LENGTH_SHORT).show()

                    }
                }

//            실제로 권한 체크
//            1) Manifest에 권한 등록 (READ_EXTERNAL_STORAGE)
//            2) 실제로 라이브러리로 질문
                TedPermission.create()
                    .setPermissionListener(permissionListener)
                    .setPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE)//Manifest여러개인데 고를 때 android 골라야 함
                    .setDeniedMessage("[설정] > [권한]에서 갤러리 권한을 열어주세요")
                    .check()

            })
            alert.setPositiveButton("취소", null)
            alert.show()
        }



        //MapView.
        //내 출발 목록 관리
        binding.myPlacesLayout.setOnClickListener {
            val myIntent = Intent(mContext, ViewMyPlaceListActivity::class.java)
            startActivity(myIntent)
        }

        // 닉네임 변경
        binding.editNicknameLayout.setOnClickListener {

            //응용문제 => AlertDialog로 닉네임을 입력받자
            //editText를 사용할 수 있는 방법2

            //PATCH - /user => field : nickname으로 보내서 닉변

            val customView = LayoutInflater.from(mContext).inflate(R.layout.my_custom_nickname_alert, null)
            val alert = AlertDialog.Builder(mContext)

            alert.setTitle("닉네임 변경")
            alert.setView(customView)
            alert.setNegativeButton("확인", DialogInterface.OnClickListener { dialogInterface, i ->
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

            alert.setPositiveButton("취소", null)
            alert.show()

        }

        //내 출발 소요 시간 변경
        binding.readyTimeLayout.setOnClickListener {

            //응용문제 => AlertDialog로 준비시간을 입력받자
            //EditText를 사용할 수 있는 방법?

            val customView = LayoutInflater.from(mContext).inflate(R.layout.my_custom_alert_edt, null)

            val alert = AlertDialog.Builder(mContext)

            alert.setTitle("외출 준비가 얼마나 걸리시나요?")
            //커스텀뷰를 가져와서 alert의 view로 설정
            alert.setView(customView)
            alert.setNegativeButton("확인", DialogInterface.OnClickListener { dialogInterface, i ->

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

            alert.setPositiveButton("취소", null)
            alert.show()

        }

    }

    override fun setValues() {
        txtTitle.text = "프로필설정"
        setUserInfo()
    }

    fun setUserInfo() {
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
            "naver" -> binding.socialLoginImg.setImageResource(R.drawable.naver_icon)
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



        //로그인한 사용자는 프로필 사진 경로(URL - String)도 들고 있다. -> profileImg에 적용 (Glide)
        Glide.with(mContext).load(GlobalData.loginUser!!.profileImgURL).into(binding.profileImg)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //갤러리에서 사진을 가져온 경우?
        if (requestCode == REQ_FOR_GALLERY) {

            //실제로 이미지를 선택한건지?
            if (resultCode == RESULT_OK) {
                //어떤 사진을 골랐는지? 파악하자 (data에 담긴듯)
                //임시 : 고른 사진을 profileImg에 바로 적용만, (서버 전송x)

                    //data? --> 이전 화면이 넘겨준 intent
                        //data?.data == 선택한 사진이 들어있는 경로 정보 (Uri)
                val dataUri = data?.data

                //Uri =-> 이미지뷰의 사진으로. (Glide)
//                Glide.with(mContext).load(dataUri).into(binding.profileImg)

                //API서버에 사진을 전송 => PUT -/user/image로 API 활용
                //파일을 같이 첨부해야 한다=> Multipart형식의 데이터 첨부 활용 (기존 FormData와는 다르다)

                //Uri -> File 형태로 변환 -> 그 파일의 실제 경로? 얻어낼 필요가 있다

                val file = File( URIPathHelper().getPath(mContext, dataUri!!) )//getPath==경로 내놔

//                파일을 Retrofit에 첨부할 수 있는 => RequestBody =>MultipartBody 형태로 변환
                val fileReqBody = RequestBody.create(MediaType.get("image/*"), file)//이건 파일 자체를 변환한거
                val body = MultipartBody.Part.createFormData("profile_image", "myFile.jpg", fileReqBody)
                //이건 이름과 파라미터를 변환시킨거
                //이제 apiService사용 가능

                apiService.putRequestProfileImg(body).enqueue(object : Callback<BasicResponse> {
                    override fun onResponse(
                        call: Call<BasicResponse>,
                        response: Response<BasicResponse>
                    ) {

                        if (response.isSuccessful) {
//                            1. 선택한 이미지로 UI 프사 변경
                            Glide.with(mContext).load(dataUri).into(binding.profileImg)
//                            2. 토스트로 성공 메시지
                            Toast.makeText(mContext, "프로필 사진이 변경되었습니다", Toast.LENGTH_SHORT).show()

                        }

                    }

                    override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                    }
                })

            }
        }

    }

}