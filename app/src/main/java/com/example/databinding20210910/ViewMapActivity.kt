package com.example.databinding20210910

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.databinding20210910.adapters.AppointmentAdapter
import com.example.databinding20210910.datas.AppointmentData
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.odsay.odsayandroidsdk.API
import com.odsay.odsayandroidsdk.ODsayData
import com.odsay.odsayandroidsdk.ODsayService
import com.odsay.odsayandroidsdk.OnResultCallbackListener

class ViewMapActivity : BaseActivity() {

    lateinit var mAppointmentData : AppointmentData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_map)
        setValues()
        setupEvent()
    }

    override fun setupEvent() {
    }

    override fun setValues() {

        mAppointmentData = intent.getSerializableExtra("appointment") as AppointmentData

        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.naverMapFrag) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.naverMapFrag, it).commit()
            }

        mapFragment.getMapAsync {

            val appointmentLatLng = LatLng(mAppointmentData.latitude, mAppointmentData.longitude)


//            지도도 약속장소로 카메라를 옮겨주자
            val cameraUpdate = CameraUpdate.scrollTo(appointmentLatLng)
            it.moveCamera(cameraUpdate)

            //마커를 찍고 표시

            val marker = Marker()

            marker.position = appointmentLatLng
            marker.map = it

//            기본적인 모양의 정보창 띄우기 (마커에 연결)
            val infoWindow = InfoWindow()
            infoWindow.adapter = object : InfoWindow.DefaultViewAdapter(mContext) {
                override fun getContentView(p0: InfoWindow): View {

                    val myView = LayoutInflater.from(mContext).inflate(R.layout.my_custom_info_window, null)

                    val placeNameTxt = myView.findViewById<TextView>(R.id.placeNameTxt)
                    val arrivalTimeTxt = myView.findViewById<TextView>(R.id.arrivalTimeTxt)

                    placeNameTxt.text = mAppointmentData.placeName
                    arrivalTimeTxt.text = "??시간 ??분 소요 예정"//

                    val myODSay = ODsayService.init(mContext, "1LXXwKUp0wg5d7YQ7MA9QAvDw59XNPYusjqx/nbI4to")

                    myODSay.requestSearchPubTransPath(
                        126.75315226866731.toString(), 37.50598420151453.toString(),
                        mAppointmentData.longitude.toString(), mAppointmentData.latitude.toString(),
                        null,null,null, object : OnResultCallbackListener{
                            override fun onSuccess(p0: ODsayData?, p1: API?) {

                                val jsonObj = p0!!.json
                                val resultObj = jsonObj.getJSONObject("result")
                                val pathArr = resultObj.getJSONArray("path")

                                /*for ( i in 0 until pathArr.length()) {
                                    val pathObj = pathArr.getJSONObject(i)
                                    Log.d("API응답", pathObj.toString(4))
                                }*/

                                val firstPath = pathArr.getJSONObject(0)
                                val infoObj = firstPath.getJSONObject("info")

                                val totalTime = infoObj.getInt("totlaTime")

                                Log.d("총 소요시간", totalTime.toString())
//                                시간/분으로 분리 => 92분 -> 1시간 32분
//                                시간 : 전체분 / 60
//                                분 : 전체문 % 60

                                val hour = totalTime/60
                                val minute = totalTime%60

                                Log.d("예상시간", hour.toString())
                                Log.d("예상분", minute.toString())


                                runOnUiThread {
                                    arrivalTimeTxt.text = "${hour}시간 ${minute}분 소요 예상"
                                }


                            }

                            override fun onError(p0: Int, p1: String?, p2: API?) {
                                //실패 시 예상 시간 받아오지 못했다는 안내
                                Log.d("예상시간 실패", p1!!)
                                arrivalTimeTxt.text = "예상시간 받아오기 실패했습니다"


                            }
                        })

                    return myView

                }
            }

            infoWindow.open(marker)

//            지도의 아무데나 찍으면 => 열려있는 마커 닫아주기
            it.setOnMapClickListener { pointF, latLng ->
                infoWindow.close()
            }

            //마커를 누를 때 : 정보창이 닫혀있으면 => 열어주자
//            열려있으면 => 닫아주자

            marker.setOnClickListener {

                val clickedMarker = it as Marker

                if (clickedMarker.infoWindow == null) {
                    //마커에 연결된 정보창이 없을 때 (정보창이 닫혀있을 때)
                    infoWindow.open(clickedMarker)
                }
                else {
                    infoWindow.close()
                }

                return@setOnClickListener true
            }

        }

    }

}