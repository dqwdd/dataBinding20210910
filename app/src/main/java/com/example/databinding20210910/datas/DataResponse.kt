package com.example.databinding20210910.datas

class DataResponse(
//    로그인 성공 시 파싱용 변수
    var user: UserData,
    var token: String,

//    이 밑으로는 약속 목록 파싱용 변수
    var appointments: List<AppointmentData>,


    //장소 목록 파싱
    var places : List<PlaceData>,

    //친구 목록
    var friends : List<UserData>
) {
}

