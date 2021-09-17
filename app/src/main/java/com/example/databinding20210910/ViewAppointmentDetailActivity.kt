package com.example.databinding20210910

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.databinding20210910.databinding.ActivityViewAppointmentDetailBinding
import com.example.databinding20210910.datas.AppointmentData
import com.example.databinding20210910.datas.PlaceData
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.overlay.PathOverlay
import com.naver.maps.map.util.MarkerIcons
import com.odsay.odsayandroidsdk.API
import com.odsay.odsayandroidsdk.ODsayData
import com.odsay.odsayandroidsdk.ODsayService
import com.odsay.odsayandroidsdk.OnResultCallbackListener
import java.text.SimpleDateFormat

class ViewAppointmentDetailActivity : BaseActivity() {

    lateinit var binding : ActivityViewAppointmentDetailBinding

    lateinit var mAppointmentData : AppointmentData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_appointment_detail)
        setValues()
        setupEvent()
    }

    override fun setupEvent() {
    }

    override fun setValues() {

        titleTxt.text = "약속 상세 확인"

        mAppointmentData = intent. getSerializableExtra("appointment") as AppointmentData

        binding.titleTxt.text = mAppointmentData.title
        binding.placeTxt.text = mAppointmentData.placeName
        binding.invitedFriendCountTxt.text = "(참여인원: ${mAppointmentData.invitedFriendList.size}명)"//문제1번


        //문제 2번
        val sdf = SimpleDateFormat("M/d a h:mm")
        binding.timeTxt.text = sdf.format(mAppointmentData.datetime)


        //문제 3번
        setNaverMap()


        //문제 4번
        val inflater = LayoutInflater.from(mContext)

        for (friend in mAppointmentData.invitedFriendList) {
            val friendView = inflater.inflate(R.layout.invited_friends_list_item, null)

            val friendPrifileImg = friendView.findViewById<ImageView>(R.id.friendProfileImg)
            val nicknameTxt = friendView.findViewById<TextView>(R.id.nicknameTxt)
            val statusTxt = friendView.findViewById<TextView>(R.id.statusTxt)

            Glide.with(mContext).load(friend.profileImgURL).into(friendPrifileImg)
            nicknameTxt.text = friend.nickName
            statusTxt.text = ""


            binding.invitedFriendsLayout.addView(friendView)

        }






        //문제 1) 참여인원 수 => "(참여인원: ?명)" 이 양식으로 => 본인 빼고 초대된 사람들의 명수만
        //문제 2) 약속 시간 => "9/3 오후 6:06" 양식으로 가공
        //문제 3) 도착지 좌표를 지도에 설정
        // --> 마커를 하나 생성 => 좌표에 찍어주기
        // ---> 카메라 이동 => 도착지 좌표로 카메라 이동
        //문제 4) 응용문제-1 친구목록을 불러서 => 레이아웃에 xml inflate해서 하나씩 addView
        //문제 5) 출발지 좌표도 지도에 설정
        // ---> 마커 찍기 3)과 동일 -> 출발지 마커 찍기 --> 1
        // ---> 출발지 / 도착지 일직선 PathOverlay그어주기 --> 2
        // ---> 대중교통 API 활용 => 1. 도착 예상 시간 표시 (infoWindow), 2. 실제 경유지로 PathOverlay 그어주기 --> 3

    }



    fun setNaverMap() {
        //지도 관련 코드
        // --> 마커를 하나 생성 => 좌표에 찍어주기
        // ---> 카메라 이동 => 도착지 좌표로 카메라 이동

        val mStartPlaceMarker = Marker()
        val selectedPointMarker = Marker()
        val mPath = PathOverlay()

        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.naverMapFrag) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.naverMapFrag, it).commit()
            }

        mapFragment.getMapAsync {

            val naverMap = it

            val marker = Marker()
            marker.icon = MarkerIcons.BLACK
            marker.iconTintColor = Color.RED
            marker.position = LatLng(mAppointmentData.latitude, mAppointmentData.longitude)
            marker.map = naverMap


            val dest = LatLng(mAppointmentData.latitude, mAppointmentData.longitude)
            //val cameraUpdate = CameraUpdate.scrollTo(dest)
            //naverMap.moveCamera(cameraUpdate)


            //문제 5) 출발지 좌표도 지도에 설정
            // ---> 마커 찍기 3)과 동일 -> 출발지 마커 찍기 --> 1
            // ---> 출발지 / 도착지 일직선 PathOverlay그어주기
            // ---> 대중교통 API 활용 => 1. 도착 예상 시간 표시 (infoWindow), 2. 실제 경유지로 PathOverlay 그어주기



            // ---> 마커 찍기 3)과 동일 -> 출발지 마커 찍기 --> 1
            val startLatLng = LatLng(mAppointmentData.startLatitude, mAppointmentData.startLongitude)

            val startMarker = Marker()
            startMarker.position = startLatLng
            startMarker.map = naverMap



            val path = PathOverlay()

            val points = ArrayList<LatLng>()
            points.add(startLatLng)

            //둘 사이에서 -> 실제 경유지들을 추가

            points.add(dest)

            path.coords = points
            path.map = naverMap





            //두 좌표의 중간점으로 카메라 이동?
            val centerOfStartAndDest = LatLng(
                (mAppointmentData.startLatitude + mAppointmentData.latitude)/2
                , (mAppointmentData.startLongitude + mAppointmentData.longitude)/2
            )

            val cameraUpdate = CameraUpdate.scrollTo(centerOfStartAndDest)
            naverMap.moveCamera(cameraUpdate)


            //거리에 따른 줌 레벨 변경 (도전 과제)

            val zoomLevel = 13.0 //두 좌표의 직선 거리에 따라 어느 줌 레벨이 적당한지 계산해줘야 함
            naverMap.moveCamera( CameraUpdate.zoomTo(zoomLevel) )


            }

        }

    }


