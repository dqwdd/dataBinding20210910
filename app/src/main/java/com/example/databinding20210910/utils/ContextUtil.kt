package com.example.databinding20210910.utils

import android.content.Context

class ContextUtil {


    companion object {

//        토큰 등을 저장하는 메모장의 파일명
        private val prefName = "FinalProjectPref"//파일 이름(메모장)

//        여러 항목 저장 가능 => 항목 명들도 변수들로 저장
//        -->일단 토큰
        val TOKEN = "TOKEN"

//        토큰 저장 / 조회

        fun setToken(context: Context, token : String) {//메모장을 열어줘야하나context, token을 string으로 set
            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            pref.edit().putString(TOKEN,  token).apply()
        }

        fun getToken(context: Context) : String {//메모장을 열어줘야하나context, token을 string으로 set
            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            return pref.getString(TOKEN, "")!!//스트링으로 내놔, TOKEN항목에 저장된걸, : String해놔서 널은 안되니 ""로 하자
        }

    }


}