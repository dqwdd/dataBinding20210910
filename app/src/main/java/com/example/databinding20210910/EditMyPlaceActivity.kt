package com.example.databinding20210910

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.databinding20210910.databinding.ActivityEditMyPlaceBinding
import com.naver.maps.map.MapFragment
import com.naver.maps.map.overlay.Marker

class EditMyPlaceActivity : BaseActivity() {

    lateinit var binding : ActivityEditMyPlaceBinding

    var mSelectedLat = 0.0
    var mSelectedLng = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_my_place)
        setupEvent()
        setValues()
    }

    override fun setupEvent() {

        binding.saveBtn.setOnClickListener {
            val inputName = binding.nameEdt.text.toString()

            //멤버변수에 있는 lat/lng 사용하자


        }

    }

    override fun setValues() {

        titleTxt.text = "내 장소 목록 관리"

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