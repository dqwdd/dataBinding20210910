package com.example.databinding20210910

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.databinding20210910.databinding.ActivityEditAppoinmentBinding

class EditAppoinmentActivity : BaseActivity() {

    lateinit var binding : ActivityEditAppoinmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_appoinment)
    }

    override fun setupEvent() {

//        1. 확인 버튼이 눌리면?
        binding.okBtn.setOnClickListener {

//            입력한 값들 받아오기
//            1. 일정 제목
            val inputTitle = binding.titleEdt.text.toString()

//            2.약속 일시 -> "2021-09-13 11:12" String 변환까지


//            3. 약속 장소?
//            - 장소 이름
            val inputPlaceName = binding.paceSearchEdt.text.toString()

//            장소/위도 ?

        }

    }

    override fun setValues() {
    }

}