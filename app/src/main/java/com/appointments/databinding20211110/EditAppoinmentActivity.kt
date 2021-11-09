package com.appointments.databinding20211110

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.setMargins
import androidx.core.view.setPadding
import androidx.databinding.DataBindingUtil
import com.appointments.databinding20211110.adapters.MyFriendSpinnerAdapter
import com.appointments.databinding20211110.adapters.StartPlaceSpinnerAdapter
import com.appointments.databinding20211110.databinding.ActivityEditAppoinmentBinding
import com.appointments.databinding20211110.datas.BasicResponse
import com.appointments.databinding20211110.datas.PlaceData
import com.appointments.databinding20211110.datas.UserData
import com.appointments.databinding20211110.services.MyJobService
import com.appointments.databinding20211110.utils.SizeUtil.Companion.dpToPx
import com.appointments.databinding20211110.utils.SizeUtil.Companion.margin
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.overlay.PathOverlay
import com.odsay.odsayandroidsdk.API
import com.odsay.odsayandroidsdk.ODsayData
import com.odsay.odsayandroidsdk.ODsayService
import com.odsay.odsayandroidsdk.OnResultCallbackListener
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class EditAppoinmentActivity : BaseActivity() {

    lateinit var binding : ActivityEditAppoinmentBinding

//    선택할 약속 일시를 저장할 변수
    val mSelectedDateTime = Calendar.getInstance()//기본값 = 현재 시간

//    선택한 약속 장소를 저장할 변수
    var mSelectedLat = 0.0 // Double을 넣을 예정
    var mSelectedLng = 0.0 // Double을 넣을 예정


//    출발지 목록을 담아줄 리스트
    val mStartPlaceList = ArrayList<PlaceData>()


//    선택된 출발지를 담아줄 변수
    lateinit var mSelectedStartPlace : PlaceData

    lateinit var mSpinnerAdapter : StartPlaceSpinnerAdapter


    //내 친구 목록을 담아줄 리스트
    val mMyFriendList = ArrayList<UserData>()
    lateinit var mSelectedFriend : UserData
    lateinit var mFriendSpinnerAdapter : MyFriendSpinnerAdapter


    //약속에 참가시킬 친구 리스트
    val mSelectedFriendList = ArrayList<UserData>()




    //선택된 출발지를 보여줄 마커
    val mStartPlaceMarker = Marker()


    //화면에 그려질 출발~도착지 연결 선
    val mPath = PathOverlay()

    //선택된 도착지를 보여줄 마커를 하나만 생성(지금은 클릭시 계속 생김(초기화 안됨))
    //22->
    val selectedPointMarker = Marker()
    //도착지에 보여줄 정보창
    val mInfoWindow = InfoWindow()



    //네이버 지도를 멤버변수로 담자
    var mNaverMap : NaverMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_appoinment)
        setupEvent()
        setValues()
    }

    override fun setupEvent() {

        //장소 찾기
        binding.placeSearchBtn.setOnClickListener {
            val inputPlaceName = binding.placeSearchEdt.text.toString()

            if (inputPlaceName.length < 2) {
                Toast.makeText(mContext, "최소 2글자 이상 입력해 주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            //다음 장소 검색 api 활용 (지정된 주소 활용) => OkHttp 직접 활용

            //1. 어디로 가야 하느냐? URL
            val url = HttpUrl.parse("https://dapi.kakao.com/v2/local/search/keyword.json")!!.newBuilder()
            url.addQueryParameter("query", inputPlaceName)

            val urlString = url.toString()



            //2. 어떤 메소드?

            //3. 어떤 파라미터 / 헤더
            val request = Request.Builder()
                .url(urlString)
                .get()
                .header("Authorization", "KakaoAK 78045276548393d2b1f8f9416fdb095c")
                .build()


            val client = OkHttpClient()
            client.newCall(request).enqueue(object : okhttp3.Callback{
                override fun onFailure(call: okhttp3.Call, e: IOException) {
                }

                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {

                    val jsonObj = JSONObject( response.body()!!.string() )
                    Log.d("장소 검색 결과", jsonObj.toString())

                    val documentsArr = jsonObj.getJSONArray("documents")

                    for ( i in 0 until documentsArr.length() ) {
                        val docu = documentsArr.getJSONObject(i)

                        Log.d("문서 아이템", docu.toString())

                        val placeName = docu.getString("place_name")
                        Log.d("장소명", placeName)

                        val lat = docu.getString("y").toDouble()
                        val lng = docu.getString("x").toDouble()
                        Log.d("위경도", "${lat} / ${lng}")



                        runOnUiThread {


                            //UI (지도 / Edt)에 반영

                            binding.placeSearchEdt.setText(placeName)

                            //지도 - 마커 찍기 + 카메라 이동
                            //좌표를 미리 생성
                            val findPlaceLatLng = LatLng(lat, lng)

                            selectedPointMarker.position = findPlaceLatLng
                            selectedPointMarker.map = mNaverMap


                            mNaverMap?.moveCamera( CameraUpdate.scrollTo(findPlaceLatLng))


                            //선택된 위경도를 변경

                            mSelectedLat = lat
                            mSelectedLng = lng

                            //길찾기 / 소요시간 등 표시 작업
                            drawStartPlaceToDestination(mNaverMap!!)



                        }



                        //임시 : 첫 번째 장소만 파싱되면 사용할 예정
                        break

                    }


                }
            })



        }



        //친구 추가 버튼
        binding.addFriendToListBtn.setOnClickListener {

            //고른 친구가 누구인지? => 스피너에서 선택되어 있는 친구를 찾아내자
            val selectedFriend = mMyFriendList [binding.myFriendSpinner.selectedItemPosition]



            //이미 선택한 친구인지 검사
            if ( mSelectedFriendList.contains(selectedFriend)) {
                Toast.makeText(mContext, "이미 추가한 친구입니다", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }




            //텍스트뷰 하나를 코틀린에서 생성
            val textView = TextView(mContext)
            textView.setBackgroundResource(R.drawable.gray_box)

            textView.setPadding(dpToPx(mContext, 5f).toInt() )


            textView.margin(right = 20F)
            //layout_example.margin(top = 20F)
            //구글링 - marginRight 설정하기 => SizeUtil 이용해서 설정

            //바로 위에 마진 코드는 적용 안되는데 이건 됨
            val params = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(dpToPx(mContext, 5f).toInt())
            textView.layoutParams = params


            textView.text = selectedFriend.nickName


            //리니어 레이아웃에 추가 + 친구 목록으로도 추가
            binding.friendListLayout.addView(textView)

            mSelectedFriendList.add(selectedFriend)




            //추가한 친구 클릭 시 추가한 친구 텍스트 삭제
            textView.setOnClickListener {
                binding.friendListLayout.removeView(textView)
                mSelectedFriendList.remove(selectedFriend)
                Toast.makeText(mContext, "${selectedFriend.nickName}님이 목록에서 삭제 되었습니다", Toast.LENGTH_SHORT).show()
            }
            
        }



        //지도 영역에 손을 대면 => 스크롤뷰 정지하게하자
//        대안 = > 지도 위에 겹쳐둔 텍스트뷰에 손을 대면 => 스크롤뷰 정지

        binding.scrollHelpTxt.setOnTouchListener { view, motionEvent ->

            binding.scrollView.requestDisallowInterceptTouchEvent(true)

            //터치 이벤트만 먹히게? x => 뒤에 가려진 지도 동작도 같이 실행되게
            return@setOnTouchListener false
        }



        //스피너의 출발지 선택 이벤트
        binding.startPlaceSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                //화면이 뜨면 자동으로 0번 아이템이 선택됨
                Log.d("선택된 위치", position.toString())

//                스피너의 위치에 맞는 장소를 선택된 출발지점으로 선정
                mSelectedStartPlace = mStartPlaceList[position]

                Log.d("출발지 위경도", "${mSelectedStartPlace.latitude}, ${mSelectedStartPlace.longitude}")

                mNaverMap?.let {
                    drawStartPlaceToDestination(it)
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }


//        일단은 날짜 선택부터
        binding.dateTxt.setOnClickListener {

//            DatePicker 띄우기 -> 입력 완료되면, 연/월/일을 제공해줌
//            mSelectedDateTime에 연/월/일 저장
            val dateSetListener = object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {

//                    선택된 날짜로서 지정
                    mSelectedDateTime.set(year, month, day)

//                    선택된 날짜로 문구 변경=> 2021-9-13 (월요일)
                    val sdf = SimpleDateFormat("yyyy. M. d (E)")
                    binding.dateTxt.text = sdf.format( mSelectedDateTime.time )

                }
            }

            val dpd = DatePickerDialog(mContext, dateSetListener,
                mSelectedDateTime.get(Calendar.YEAR),
                mSelectedDateTime.get(Calendar.MONTH),
                mSelectedDateTime.get(Calendar.DAY_OF_MONTH))

            dpd.show()

        }



//        시간 선택
        binding.timeTxt.setOnClickListener {

//            TimePicker띄우기 -> 입력 완료되면 시/분 제공
//            mSelectedTime에 시/분 저장

            val tsl = object  : TimePickerDialog.OnTimeSetListener {
                override fun onTimeSet(p0: TimePicker?, hour: Int, minute: Int) {

                    mSelectedDateTime.set(Calendar.HOUR_OF_DAY, hour)
                    mSelectedDateTime.set(Calendar.MINUTE, minute)

//                    오후 6:05  형태로 가공. => SimpleDateFormat
                    val sdf = SimpleDateFormat("a h:mm")
                    binding.timeTxt.text = sdf.format(mSelectedDateTime.time)

                }

            }

            TimePickerDialog(mContext, tsl,
                mSelectedDateTime.get(Calendar.HOUR_OF_DAY),
                mSelectedDateTime.get(Calendar.MINUTE),
                false).show()

        }



//        1. 확인 버튼이 눌리면?
        binding.okBtn.setOnClickListener {

//            입력한 값들 받아오기
//            1. 일정 제목
            val inputTitle = binding.titleEdt.text.toString()

//            2.약속 일시 -> "2021-09-13 11:12" String 변환까지
//             => 날짜 / 시간 중 선택 안한게 있다면? 선택하라고 토스트, 함수 강제 종료(validation)
            if (binding.dateTxt.text == "약속 일자") {
                Toast.makeText(mContext, "일자를 설정하지 않았습니다", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (binding.timeTxt.text == "약속 시간") {
                Toast.makeText(mContext, "시간을 설정하지 않았습니다", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

//            여기 코드 실행 된다 : 일자 / 시간 모두 설정했다.



//            약속 일시를 => UTC 시간대로 변경해주자. 서버가 사용하는 시간대는 UTC라서
//            앱에서 폰의 시간대를 찾아서 보정해주자
//            어떻게찾냐?
            val myTimeZone = mSelectedDateTime.timeZone//이게 내 폰의 시간대
//            내 시간대가 시차가 utc로 부터 얼마나 나느냐~
            val myTimeOffset = myTimeZone.rawOffset / 1000 / 60 / 60 //밀리초에서 시간으로 변환
            //선택된 시간을 보정(더해져 있는 시차를 빼주자)
            mSelectedDateTime.add(Calendar.HOUR_OF_DAY, -myTimeOffset)//빼주자는 없는데 더해주자는 있음


//            선택된 약속일시를 -> "yyyy-MM-dd HH:mm"양식으로 가공(스웨거에 나온 양식) => 최종 서버에 파라미터로 첨부
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
            val finalDatetime = sdf.format(mSelectedDateTime.time)

            Log.d("서버에 보낼 약속 일시", finalDatetime)



//            3. 약속 장소?
//            - 장소 이름
            val inputPlaceName = binding.placeSearchEdt.text.toString()

//            장소 위도/경도 ?
//            val lat = 37.57795738970527
//            val lng = 127.03360068706621

//            지도에서 클릭한 좌표로 위경도 첨부
//            선택 안했다면? 선택해달라고 안내

            if ( mSelectedLat == 0.0 && mSelectedLng == 0.0) {
                Toast.makeText(mContext, "약속 장소를 지도에서 선택해 주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


//             출발지 정보도 같이 첨부

//            선택한 친구 목록? "1, 3, 5" 가공해서 첨부
            var friendListStr =  ""
//            friendListStr에 들어갈 String을 친구 목록을 이용해 가공

            for (friend in mSelectedFriendList) {
                Log.d("친구들", friend.id.toString())

                friendListStr += friend.id
                friendListStr += ","
            }

            //마지막의 ,를 제거  => 글자의 길이가 0보다 커야 수행 가능함
            if (friendListStr != "") {
                friendListStr = friendListStr.substring(0, friendListStr.length - 1)
            }
//            if (friendListStr != "") {
//                friendListStr = friendListStr
//            }

            Log.d("첨부할 친구 목록", friendListStr)




//            서버에 API 호출
            apiService.postRequestAppointment(
//                ContextUtil.getToken(mContext),
                inputTitle,
                finalDatetime,
                mSelectedStartPlace.name,
                mSelectedStartPlace.latitude,
                mSelectedStartPlace.longitude,
                inputPlaceName,
                mSelectedLat, mSelectedLng, friendListStr).enqueue(object : Callback<BasicResponse> {
                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {

                    if ( response.isSuccessful ) {

                        //임시 : 1분 후에 교통 상황 파악하는 작업 예약 => JobScheduler 클래스
                        //실제 : 약속시간 2~3시간 전에 교통 상황 파악 작업 예약


                        //예약을 걸도록 도와주는 도구
                        val js = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler

                        //실제로 예약시간이 되면 어떤 일을 할 지 적어둔 클래스 필요
                        //백그라운드 작업을 가정해야 함 => 서비스 클래스 작업이 필요
                        val serviceComponent = ComponentName(mContext, MyJobService::class.java)


                        //"언제?" 어떤 일을? 모아주는 클래스
                        //언제? (약속시간 - 2시간) - 현재시간 => 이 시차만큼 지나면 실행되도록
                        //약속시간 : 시차 보정 => 2시간 빼주자
                        mSelectedDateTime.add(Calendar.HOUR_OF_DAY, -2)


                        //현재 시간 : 시차 보정 x => 시차 보정
                        val now = Calendar.getInstance()
                        val timeOffset = now.timeZone.rawOffset / 1000 / 60 / 60
                        now.add(Calendar.HOUR_OF_DAY, -timeOffset)

                        //필요한 시간이 지나면 예약 작업이 실행되도록
                        val jobTime = mSelectedDateTime.timeInMillis - now.timeInMillis


                        //jobInfo=>ID값을 넣을 수 있다. 약속의 ID 값을 넣어보자
                        //약속 작성 화면 => 만든 약속의 id값?(우리가 알 수 없음-서버에게 받은거 봐야지)
                        val basicResponse = response.body()!!


                        val jobInfo = JobInfo.Builder(basicResponse.data.appointment.id, serviceComponent)
                            .setMinimumLatency(jobTime)//약속시간 기준으로 2시간 전이면 실행
//                            .setMinimumLatency(TimeUnit.SECONDS.toMillis(20))//위의 코드가 너무 오래 걸려서 20초 뒤에로 바꿈

//                            .setMinimumLatency(TimeUnit.MINUTES.toMillis(1))
                            // 얼마 후에 실행 할건지?, 임시 1분, 약속 시간 기준으로
                            // -> 약속 시간 기준으로 하려면 그게 몇 분 후 인지 계산 필요
                            .setOverrideDeadline(TimeUnit.MINUTES.toMillis(3))
                        //1분 후 : 대략 1분 후 => 3분 정도까지만 기다리자 => 안드로이드가 배터리 이슈로 정확한 시간 계산을 막아놈
                            .build()

                        //예약 도구를 이용해 스케줄 설정
                        js.schedule(jobInfo)



                        Toast.makeText(mContext, "약속이 등록되었습니다", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    else {
                        Toast.makeText(mContext, "뭔가 오류남", Toast.LENGTH_SHORT).show()
                        Log.d("뭔가 오류남", friendListStr)
                    }
                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                    //
                }
            })


        }

    }

    override fun setValues() {
        txtTitle.text = "약속설정"

        mFriendSpinnerAdapter = MyFriendSpinnerAdapter(mContext, R.layout.friend_list_item, mMyFriendList)
        binding.myFriendSpinner.adapter = mFriendSpinnerAdapter


        //친구 목록 받아오기
        apiService.getRequestFriendList("my").enqueue(object : Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if (response.isSuccessful) {
                    mMyFriendList.clear()
                    mMyFriendList.addAll(response.body()!!.data.friends)
                    mFriendSpinnerAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }
        })






        mSpinnerAdapter = StartPlaceSpinnerAdapter(mContext, R.layout.my_place_list_item, mStartPlaceList)
        binding.startPlaceSpinner.adapter = mSpinnerAdapter

//        내 출발 장소 목록 담아주기

        apiService.getRequestMyPlaceList().enqueue(object : Callback<BasicResponse>{
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if (response.isSuccessful) {
                    val basicResponse = response.body()!!

                    mStartPlaceList.clear()
                    mStartPlaceList.addAll(basicResponse.data.places)

                    mSpinnerAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
            }
        })



//        카카오지도 띄워보기
//        val mapView = MapView(mContext)
//
//
//        binding.mapView.addView(mapView)


//        네이버 지도 띄워보기(네이버지도 프래그먼트 다루기)
        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.naverMapView) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.naverMapView, it).commit()
            }

        mapFragment.getMapAsync {
            Log.d("지도 객체-바로 할 일", it.toString())

            //멤버변수에서 null이던 네이버지도 변수를 채워넣기
            mNaverMap=it
//            학원 좌표를 지도 시작점으로(예제)
//            it.mapType = NaverMap.MapType.Hybrid//그냥 해본거(다른 형태의 지도로 보기)


//            좌표를 다루는 변수 - LatLng클래스 활용
            val neppplusCoord = LatLng(37.50586305676978, 126.75316685445878)

            val cameraUpdate = CameraUpdate.scrollTo(neppplusCoord)
            it.moveCamera(cameraUpdate)

            val uiSettings = it.uiSettings
            uiSettings.isCompassEnabled = true
            uiSettings.isScaleBarEnabled = false



            selectedPointMarker.icon = OverlayImage.fromResource(R.drawable.marker_pin_icon)

            it.setOnMapClickListener { pointF, latLng ->
               // Toast.makeText(mContext, "위도 : ${latLng.latitude}, 경도 : ${latLng.longitude}"
                 //   , Toast.LENGTH_SHORT).show()

                mSelectedLat = latLng.latitude
                mSelectedLng = latLng.longitude


                //좌표를 받아서 => 마커를 생성해서 => 맵에 띄우자
                //22--> 미리 연결해둔 마커의 좌표로 연결하자(마커 지금 클릭 되는 대로 눌러지는데 1개만 되게)
                //즉 마커를 미리 만들어서 누를 떄 그 위치에 마커를 갖다 놓는거임
//                val marker = Marker(LatLng(mSelectedLat, mSelectedLng))
//                marker.map = it

//                22의 코드
                selectedPointMarker.position = LatLng(mSelectedLat, mSelectedLng)
                selectedPointMarker.map = it


                drawStartPlaceToDestination(it)

            }
            
        }

    }

    //        시작 지점의 위경도
    fun drawStartPlaceToDestination(naverMap: NaverMap) {

        //mSelectedStartPlace.latitude등 활용

        //시작 지점의 좌표(마커) 찍어주기
        mStartPlaceMarker.position = LatLng(mSelectedStartPlace.latitude, mSelectedStartPlace.longitude)
        mStartPlaceMarker.map = naverMap
        //위에서 마커 선언하고 이거만 씀


//        도착 지점의 위경도
        //mSelectedLnt 등 활용

//        (예제) 시작지점 -> 도착지점으로 연결 선 그어주기

        // 좌표 목록을 ArrayList로 담자
        val points = ArrayList<LatLng>()

        //출발지점의 좌표를 선의 출발점으로 설정
        points.add( LatLng(mSelectedStartPlace.latitude, mSelectedStartPlace.longitude) )

        //대중교통 길찾기 API -> 들리는 좌표들을 제공 -> 목록을 담아주자
        val odsay = ODsayService.init(mContext, "1LXXwKUp0wg5d7YQ7MA9QAvDw59XNPYusjqx/nbI4to")

        odsay.requestSearchPubTransPath(
            mSelectedStartPlace.longitude.toString(), mSelectedStartPlace.latitude.toString(),
            mSelectedLng.toString(), mSelectedLat.toString(), null, null, null,
            object : OnResultCallbackListener{//갔다와서 뭐할건가여ㅛ
                override fun onSuccess(p0: ODsayData?, p1: API?) {

                    val jsonObj = p0!!.json
                    val resultObj = jsonObj.getJSONObject("result")
                    val pathArr = resultObj.getJSONArray("path")
                    val firstPathObj = pathArr.getJSONObject(0)

                    //총 소요시간이 얼마나 걸리나?
                    Log.d("길찾기 응답", firstPathObj.toString())
                    val infoObj = firstPathObj.getJSONObject("info")
                    val totalTime = infoObj.getInt("totalTime")

                    Log.d("총 소요시간 : ", totalTime.toString())//



                    //멤버변수로 만들어둔 정보창의 내용 설정, 열어주기
                    mInfoWindow.adapter = object : InfoWindow.DefaultTextAdapter(mContext) {
                        override fun getText(p0: InfoWindow): CharSequence {
                            return "${totalTime}분 소요 예정"
                        }

                    }
                    mInfoWindow.open(selectedPointMarker)


                    //경유지들 좌표를 목록에 추가 ( 결과가 어떻게 되어있는지 분석. parsing)
                    //지도에 선을 긋는데 필요한 좌표 목록 추출
                    val subPathArr = firstPathObj.getJSONArray("subPath")

                    for ( i in 0 until subPathArr.length()) {
                        val subPathObj = subPathArr.getJSONObject(i)

                        if (!subPathObj.isNull("passStopList")) {

                            //정거장 목록을 불러내보자
                            val passStopListObj = subPathObj.getJSONObject("passStopList")
                            val stationsArr = passStopListObj.getJSONArray("stations")

                            for ( j in 0 until stationsArr.length()) {

                                val stationObj = stationsArr.getJSONObject(j)

                                Log.d("길찾기 응답", stationObj.toString())

                                val latLng = LatLng(stationObj.getString("y").toDouble(), stationObj.getString("x").toDouble())

                                //points ArrayList에 경유지로 추가
                                points.add(latLng)


                            }

                        }


                    }

                    //최종 목적지 좌표도 추가

                //최종 목적지 추가
                points.add( LatLng(mSelectedLat, mSelectedLng) )

                //매번 새로 PollyLine을 그리면, 선이 하나씩 추가됨
                //멤버변수로 선을 하나 지정해두고, 위치값만 변경하면서 사용
                //val polyline = PolylineOverlay()

                mPath.coords = points

                mPath.map = naverMap

                }

                override fun onError(p0: Int, p1: String?, p2: API?) {
                }
            })
    }

}