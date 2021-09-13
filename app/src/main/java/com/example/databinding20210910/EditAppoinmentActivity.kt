package com.example.databinding20210910

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.databinding.DataBindingUtil
import com.example.databinding20210910.databinding.ActivityEditAppoinmentBinding
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*

class EditAppoinmentActivity : BaseActivity() {

    lateinit var binding : ActivityEditAppoinmentBinding

//    선택할 약속 일시를 저장할 변수
    val mSelectedDateTime = Calendar.getInstance()//기본값 = 현재 시간
//    val mSelectedTime = Ti

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_appoinment)
    }

    override fun setupEvent() {

//        일단은 날짜 선택부터
        binding.dateTxt.setOnClickListener {

//            DatePicker 띄우기 -> 입력 완료되면, 연/월/일을 제공해줌
//            mSelectedDateTime에 연/월/일 저장
            val dateSetListener = object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {

//                    선택된 날짜로서 지정
                    mSelectedDateTime.set(year, month, day)

//                    선택된 날짜로 문구 변경=> 2021-9-13 (월요일)
                    val sdf = SimpleDateFormat("yyyy-MM-dd (E)")
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

        }



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