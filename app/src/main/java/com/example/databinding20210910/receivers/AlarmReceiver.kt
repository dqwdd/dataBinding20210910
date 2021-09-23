package com.example.databinding20210910.receivers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log

class AlarmReceiver : BroadcastReceiver() {

    companion object {

        val ALARM_ID = 1001
        val PRIMARY_CHANNEL_ID = "PRIMARY_CHANNEL_ID"

    }

    //알람을 관리하는 클래스를 멤버변수로 선언
    lateinit var mNotificationManager : NotificationManager


    override fun onReceive(context: Context, intent: Intent?) {

        Log.d("알람 울림", "테스트로그")
        mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        //알림 - Oreo버전 이후로는 채널을 선정해야 함. 그 이전 버전에서는 하면 안됨
        createNotificationChannel()

    }

    fun createNotificationChannel() {
        //폰에 깔린 OS가 오레오 버전 이상인가?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            //맞을 때만 알림 채널 설정
            val notiChannel = NotificationChannel(PRIMARY_CHANNEL_ID,
                "Primary Channel",
                NotificationManager.IMPORTANCE_HIGH)

            notiChannel.enableLights(true)
            notiChannel.lightColor = Color.RED

            notiChannel.enableVibration(true)//진동
            notiChannel.description = "알람을 통한 Notification 테스트"//설명 문구


            //알림(Notification) 매니저를 통해서 채널을 등록해 줘야 함
            mNotificationManager.createNotificationChannel(notiChannel)



        }


    }



}