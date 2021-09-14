package com.example.databinding20210910

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.example.databinding20210910.adapters.AppointmentAdapter
import com.example.databinding20210910.datas.AppointmentData
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker

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

                    return myView

                }
            }
            infoWindow.open(marker)


        }

    }

}