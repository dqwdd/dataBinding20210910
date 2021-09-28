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

            val url = HttpUrl.parse("https://dapi.kakao.com/v2/local/search/keyword.json")!!.newBuilder()
            url.addQueryParameter("query", inputPlaceName)

            val urlString = url.toString()

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

                        val placeName = docu.getString("place_name")

                        val lat = docu.getString("y").toDouble()
                        val lng = docu.getString("x").toDouble()


                        runOnUiThread {
                            //UI (지도 / Edt)에 반영
                            binding.placeSearchEdt.setText(placeName)

                            val findPlaceLatLng = LatLng(lat, lng)

                            selectedPointMarker.position = findPlaceLatLng
                            selectedPointMarker.map = mNaverMap

                            mNaverMap?.moveCamera( CameraUpdate.scrollTo(findPlaceLatLng))

                            mSelectedLat = lat
                            mSelectedLng = lng
                        }

                        break
                    }
                }
            })

        }


        binding.dateTxt.setOnClickListener {

            val dateSetListener = object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {

                    mSelectedDateTime.set(year, month, day)

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


        binding.modifyBtn.setOnClickListener {

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

        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map_fragment) as MapFragment?
            ?:MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map_fragment, it).commit()
            }

        mapFragment.getMapAsync {

            val marker = Marker()

            it.setOnMapClickListener { pointF, latLng ->
                mSelectedLat = latLng.latitude
                mSelectedLng = latLng.longitude

                marker.position = latLng
                marker.map = it
            }

        }


    }

}