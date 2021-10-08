package com.appointments.databinding20210910.utils

import android.content.Context
import android.util.Log
import com.appointments.databinding20210910.datas.UserData


class GlobalData {


    companion object {

        var context : Context? = null


//        로그인한 사용자를 담아둘 변수
        var loginUser : UserData? = null//앱이 처음 켜졌을 때는 로그인 한 사용자가 없다는걸 표시한 코드(null)
//        기본값으로는 로그인 한 사람이 없다. null로 미리 대입
//        그냥 null로 넣으면 넣을 데이터가 유추가 안된다.(코틀린)
//        로그인 한 사람이 누구인지 -> GlobalData에 저장


        set(value) {

            //부가적으로 할 행동
            value?.let {

                //유저 데이터가 null이 아님 => 로그인등의 이유로 사용자 기록
                //내 준비 시간을 => ContextUtil에 기록해두자

                ContextUtil.setMyReadyMinute(context!!, it.readyMinute)
                Log.d("사용자닉네임", it.nickName)

            }//value가 있으면 이걸 실행 해주세요


            if (value==null) {
                //로그아웃 등의 이유로 데이터 파기
                //내 준비시간을 0(기본값)으로 되돌리기
                ContextUtil.setMyReadyMinute(context!!, 0)
            }

            //실제 변수에 입력값 대입
            field = value

        }//value를 받아왔을 때 어떻게 할거냐~


    }


}