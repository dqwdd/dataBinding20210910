package com.example.databinding20210910

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.databinding20210910.databinding.ActivityEditAppoinmentBinding
import com.example.databinding20210910.datas.BasicResponse
import com.example.databinding20210910.utils.ContextUtil
import net.daum.mf.map.api.MapView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*

class EditAppoinmentActivity : BaseActivity() {

    lateinit var binding : ActivityEditAppoinmentBinding

//    선택할 약속 일시를 저장할 변수
    val mSelectedDateTime = Calendar.getInstance()//기본값 = 현재 시간

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_appoinment)
        setupEvent()
        setValues()
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
//            선택된 약속일시를 -> "yyyy-MM-dd HH:mm"양식으로 가공(스웨거에 나온 양식) => 최종 서버에 파라미터로 첨부
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
            val finalDatetime = sdf.format(mSelectedDateTime.time)

            Log.d("서버에 보낼 약속 일시", finalDatetime)



//            3. 약속 장소?
//            - 장소 이름
            val inputPlaceName = binding.paceSearchEdt.text.toString()

//            장소 위도/경도 ?
            val lat = 37.57795738970527
            val lng = 127.03360068706621



//            서버에 API 호출
            apiService.postRequestAppointment(
//                ContextUtil.getToken(mContext),
                inputTitle,
                finalDatetime,
                inputPlaceName, lat, lng).enqueue(object : Callback<BasicResponse> {
                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {

                    if ( response.isSuccessful ) {
                        Toast.makeText(mContext, "약속이 등록되었습니다", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    else {
                        Toast.makeText(mContext, "뭔가 오류남", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                    //
                }
            })


        }

    }

    override fun setValues() {

//        카카오지도 띄워보기
//        val mapView = MapView(mContext)
//
//
//        binding.mapView.addView(mapView)


    }

}