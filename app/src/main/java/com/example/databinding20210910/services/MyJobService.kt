package com.example.databinding20210910.services

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.os.SystemClock
import android.util.Log
import com.example.databinding20210910.receivers.AlarmReceiver
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

        //알람을 울리게 도와주는 도구 => BroadCast 송신
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        //실제로 알람이 울리면 실행할 코드 => BroadCast 수신시 => BroadcastReceiver에 작업 해 둘 필요가 있다.
        val myIntent = Intent(this, AlarmReceiver::class.java)//receivers패키지 만듬


        //할 일을 가지고 대기(Pending) 해주는 Intent
        val pendingIntent = PendingIntent.getBroadcast(this,
            AlarmReceiver.ALARM_ID,
            myIntent,
            PendingIntent.FLAG_UPDATE_CURRENT)


        //알람이 울릴 시간 설정 (임시로 1분 후로 설정 함)
        val now = Calendar.getInstance()//현재 시간 가져오기
        val triggerTime = SystemClock.elapsedRealtime() + 60 * 1000

        //실제 알람 시간 : 교통 소요 시간 (API), 내 준비 시간 고려





        //실제 알람 기능 설정
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, pendingIntent)
        //핸드폰이 잠겨있어도 울리게 해주는거(==WAKEUP)

        return false
    }

    override fun onStopJob(p0: JobParameters?): Boolean {



        return false
    }
}