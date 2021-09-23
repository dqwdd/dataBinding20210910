package com.example.databinding20210910.services

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.example.databinding20210910.R
import com.example.databinding20210910.datas.BasicResponse
import com.example.databinding20210910.receivers.AlarmReceiver
import com.example.databinding20210910.utils.ContextUtil
import com.example.databinding20210910.utils.GlobalData
import com.example.databinding20210910.web.ServerAPI
import com.example.databinding20210910.web.ServerAPIService
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.PathOverlay
import com.odsay.odsayandroidsdk.API
import com.odsay.odsayandroidsdk.ODsayData
import com.odsay.odsayandroidsdk.ODsayService
import com.odsay.odsayandroidsdk.OnResultCallbackListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MyJobService : JobService() {

    companion object {

        //어떤 작업인지 구별하기 쉽게 숫자를 변수로 담자
        val JOB_TIME_SET = 1000


    }


    override fun onStartJob(p0: JobParameters?): Boolean {

        Log.d("예약된 작업 시작", p0!!.jobId.toString())


        //임시로 1분 후 알람 설정 (테스트용)
        //실제 : 약속시간 - (API에서 알려준) 교통 소요시간 - 내 준비시간 계산된 시간에 알람
        //jobId => 약속정보 (출발/도착 좌표) => 상세정보 확인 API를 통해 좌표 정보도 받아오자
        val retrofit =  ServerAPI.getRetrofit(applicationContext)
        val apiService = retrofit.create(ServerAPIService::class.java)
        apiService.getRequestAppointmentDetail(p0!!.jobId).enqueue(object : Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {

                //예약된 작업에서 => 서버에 약속 정보를 요청
                if (response.isSuccessful) {

                    val basicResponse =response.body()!!
                    //서버가 알려준 약속 데이터 => 교통정보 확인 => 알람 설정
                    val appointmentData = basicResponse.data.appointment

                    val myODsayService = ODsayService.init(applicationContext, "1LXXwKUp0wg5d7YQ7MA9QAvDw59XNPYusjqx/nbI4to")

                    myODsayService.requestSearchPubTransPath(
                        appointmentData.startLongitude.toString(),
                        appointmentData.startLatitude.toString(),
                        appointmentData.longitude.toString(),
                        appointmentData.latitude.toString(),
                        null,
                        null,
                        null,
                        object : OnResultCallbackListener {
                            override fun onSuccess(p0: ODsayData?, p1: API?) {

                                val jsonObj = p0!!.json
                                val resultObj = jsonObj.getJSONObject("result")
                                val pathArr = resultObj.getJSONArray("path")


                                val firstPath = pathArr.getJSONObject(0)

                                val infoObj = firstPath.getJSONObject("info")

                                val totalTime = infoObj.getInt("totalTime")

                                Log.d("총 소요시간", totalTime.toString())

                                val hour = totalTime / 60
                                val minute = totalTime % 60

                                Log.d("예상시간", hour.toString())
                                Log.d("예상분", minute.toString())

                                //예상 시간이 몇분이나 걸리는지 파악 완료 => 알람 띄우는데 활용

                                //알람 시간 : 약속시간(타임존에 맞게 변경) - (교통소요시간 + 내 준비 시간)(밀리초단위)
                                val now = Calendar.getInstance()
                                appointmentData.datetime.time += now.timeZone.rawOffset
                                val alarmTime = appointmentData.datetime.time -
                                        (totalTime*60*1000 - ContextUtil.getMyReadyMinute(applicationContext) * 60 * 1000)
                                        //appointmentData.datetime.time==Long타입임
                                setAlarmByMilliSecond(alarmTime)

                            }

                            override fun onError(p0: Int, p1: String?, p2: API?) {
//                                실패시 예상시간 받아오지 못했다는 안내.
                                Log.d("예상시간실패", p1!!)
                            }
                        })

                }


            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
            }
        })



        return false
    }



    //언제 알람을 울릴지 계산해서 넘겨주면 단순히 울리기만 하는 함수
    fun setAlarmByMilliSecond(timeInMillis: Long) {
        //알람을 울리게 도와주는 도구 => BroadCast 송신
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        //실제로 알람이 울리면 실행할 코드 => BroadCast 수신시 => BroadcastReceiver에 작업 해 둘 필요가 있다.
        val myIntent = Intent(this, AlarmReceiver::class.java)//receivers패키지 만듬


        //할 일을 가지고 대기(Pending) 해주는 Intent
        val pendingIntent = PendingIntent.getBroadcast(this,
            AlarmReceiver.ALARM_ID,
            myIntent,
            PendingIntent.FLAG_UPDATE_CURRENT)

        //알람이 울릴 시간 설정(위의 함수에서 계산해서 내려줌)
        val triggerTime = timeInMillis

        //알람이 울릴 시간 설정 (임시로 30초 후로 설정 함)
        //val triggerTime = SystemClock.elapsedRealtime() + 60 * 1000

        //실제 알람 시간 : 교통 소요 시간 (API), 내 준비 시간 고려
        //출발지 좌표 / 약속장소 좌표 필요


        //실제 알람 기능 설정
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, pendingIntent)
        //핸드폰이 잠겨있어도 울리게 해주는거(==WAKEUP)
    }



    override fun onStopJob(p0: JobParameters?): Boolean {

        return false
    }
}