package com.example.databinding20210910.utils

import com.example.databinding20210910.datas.UserData


class GlobalData {


    companion object {

//        로그인한 사용자를 담아둘 변수
        var loginUser : UserData? = null//앱이 처음 켜졌을 때는 로그인 한 사용자가 없다는걸 표시한 코드(null)
//        기본값으로는 로그인 한 사람이 없다. null로 미리 대입
//        그냥 null로 넣으면 넣을 데이터가 유추가 안된다.(코틀린)
//        로그인 한 사람이 누구인지 -> GlobalData에 저장



    }


}