package com.example.databinding20210910

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.databinding20210910.adapters.MyFriendSpinnerAdapter
import com.example.databinding20210910.adapters.StartPlaceSpinnerAdapter
import com.example.databinding20210910.databinding.ActivityModifyAppointmentBinding
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
import com.odsay.odsayandroidsdk.API
import com.odsay.odsayandroidsdk.ODsayData
import com.odsay.odsayandroidsdk.ODsayService
import com.odsay.odsayandroidsdk.OnResultCallbackListener
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ModifyAppointmentActivity : BaseActivity() {


    lateinit var binding : ActivityModifyAppointmentBinding

    lateinit var mAppointmentData : AppointmentData


    val mSelectedDateTime = Calendar.getInstance()//기본값 = 현재 시간


    //선택된 출발지를 보여줄 마커
    val mStartPlaceMarker = Marker()


    //화면에 그려질 출발~도착지 연결 선
    val mPath = PathOverlay()

    val selectedPointMarker = Marker()
    val mInfoWindow = InfoWindow()


    //네이버 지도를 멤버변수로 담자
    var mNaverMap : NaverMap? = null

    var mSelectedLat = 0.0 // Double을 넣을 예정
    var mSelectedLng = 0.0 // Double을 넣을 예정


    val mStartPlaceList = ArrayList<PlaceData>()

    lateinit var mSelectedStartPlace : PlaceData

    lateinit var mSpinnerAdapter : StartPlaceSpinnerAdapter




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_modify_appointment)
        setValues()
        setupEvent()
    }

    override fun setupEvent() {

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


        binding.saveBtn.setOnClickListener {

            if (binding.dateTxt.text == "약속 일자") {
                Toast.makeText(mContext, "일자를 설정하지 않았습니다", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (binding.timeTxt.text == "약속 시간") {
                Toast.makeText(mContext, "시간을 설정하지 않았습니다", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
            val finalDatetime = sdf.format(mSelectedDateTime.time)

            val inputPlaceName = binding.placeSearchEdt.text.toString()

            if ( mSelectedLat == 0.0 && mSelectedLng == 0.0) {
                Toast.makeText(mContext, "약속 장소를 지도에서 선택해 주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

        }

    }

    override fun setValues() {

        titleTxt.text = "약속 수정"


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
            object : OnResultCallbackListener {//갔다와서 뭐할건가여ㅛ
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