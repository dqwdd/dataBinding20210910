package com.example.databinding20210910

import android.Manifest
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.databinding20210910.databinding.ActivityViewAppointmentDetailBinding
import com.example.databinding20210910.datas.AppointmentData
import com.example.databinding20210910.datas.PlaceData
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
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
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
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

        binding.arrivalBtn.setOnClickListener {

            //내 위치를 파악. (현재 위치의 위도/경도 추출)
            //위치를 받아도 될지 권한부터 물어보자
            val pl = object : PermissionListener {
                override fun onPermissionGranted() {
                    //실제 위치 물어보기 (안드로이드 폰에게)
                }

                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                    Toast.makeText(mContext, "현재 위치 정보를 파악해야 약속 도착 인증이 가능합니다", Toast.LENGTH_SHORT).show()
                }
            }
            TedPermission.create()
                .setPermissionListener(pl)
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)//Manifest뜨는거 android 골라야 함
                .check()
        }


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








            // 대중교통 API 활용 => 1. 도착시간 예상시간 표시 (infoWindow)
            //2. 실제 경유지로 PathOverlay 그어주기 => 도전과제

            val infoWindow = InfoWindow()
            val myODsayService = ODsayService.init(mContext, "1LXXwKUp0wg5d7YQ7MA9QAvDw59XNPYusjqx/nbI4to")

            myODsayService.requestSearchPubTransPath(
                mAppointmentData.startLongitude.toString(),
                mAppointmentData.startLatitude.toString(),
                mAppointmentData.longitude.toString(),
                mAppointmentData.latitude.toString(),
                null,
                null,
                null,
                object : OnResultCallbackListener {
                    override fun onSuccess(p0: ODsayData?, p1: API?) {

                        val jsonObj = p0!!.json
                        val resultObj = jsonObj.getJSONObject("result")
                        val pathArr = resultObj.getJSONArray("path")


                        val firstPath = pathArr.getJSONObject(0)


                        //출발점 ~ 경유지 목록 ~ 도착지를 이어주는 Path 객체를 추가
                        val points = ArrayList<LatLng>()
                        // 출발지부터 추가
                        points.add( LatLng(mAppointmentData.startLatitude, mAppointmentData.startLongitude) )

//                        경유지목록 파싱 -> for문으로 추가.
                        val subPathArr = firstPath.getJSONArray("subPath")
                        for ( i in 0 until subPathArr.length()) {
                            val subPathObj = subPathArr.getJSONObject(i)
                            Log.d("응답 내용", subPathObj.toString())

                            if (!subPathObj.isNull("passStopList")) {
                                val passStopListObj = subPathObj.getJSONObject("passStopList")
                                val stationsArr = passStopListObj.getJSONArray("stations")
                                for ( j in 0 until stationsArr.length()) {
                                    val stationObj = stationsArr.getJSONObject(j)
                                    Log.d("정거장목록", stationObj.toString())

                                    //각 정거장의 GPS 추출 -> 네이버지도의 위치객체로 변환
                                    val latLng = LatLng(stationObj.getString("y").toDouble(), stationObj.getString("x").toDouble())

                                    //지도의 선을 긋는 좌표 목록에 추가
                                    points.add(latLng)
                                }
                            }

                        }
                        //모든 정거장이 추가됨 -> 실제 목적지 좌표 추가
                        points.add( LatLng(mAppointmentData.latitude, mAppointmentData.longitude) )
                        //모든 경로 설정 끝 -> 네이버지도에 선으로 이어주자
                        val path = PathOverlay()
                        path.coords = points
                        path.map = naverMap


                        val infoObj = firstPath.getJSONObject("info")

                        val totalTime = infoObj.getInt("totalTime")

                        Log.d("총 소요시간", totalTime.toString())

                        val hour = totalTime / 60
                        val minute = totalTime % 60

                        Log.d("예상시간", hour.toString())
                        Log.d("예상분", minute.toString())

                        infoWindow.adapter = object : InfoWindow.DefaultViewAdapter(mContext) {
                            override fun getContentView(p0: InfoWindow): View {

                                val myView = LayoutInflater.from(mContext).inflate(R.layout.my_custom_info_window, null)

                                val placeNameTxt = myView.findViewById<TextView>(R.id.placeNameTxt)
                                val arrivalTimeTxt = myView.findViewById<TextView>(R.id.arrivalTimeTxt)

                                placeNameTxt.text = mAppointmentData.placeName

                                if (hour == 0) {
                                    arrivalTimeTxt.text = "${minute}분 소요 예정"
                                }
                                else {
                                    arrivalTimeTxt.text = "${hour}시간 ${minute}분 소요 예정"
                                }

                                return myView
                            }

                        }

                        infoWindow.adapter = object : InfoWindow.DefaultViewAdapter(mContext) {
                            override fun getContentView(p0: InfoWindow): View {

                                val myView = LayoutInflater.from(mContext).inflate(R.layout.my_custom_info_window, null)

                                val placeNameTxt = myView.findViewById<TextView>(R.id.placeNameTxt)
                                val arrivalTimeTxt = myView.findViewById<TextView>(R.id.arrivalTimeTxt)

                                placeNameTxt.text = mAppointmentData.placeName
//                    arrivalTimeTxt.text = "??시간 ?분 소요예상"

                                return myView
                            }
                        }

                        infoWindow.open(marker)

                    }

                    override fun onError(p0: Int, p1: String?, p2: API?) {
//                                실패시 예상시간 받아오지 못했다는 안내.
                        Log.d("예상시간실패", p1!!)
                    }
                })


            }

        }

    }


