package com.example.databinding20210910.datas

class DataResponse(
//    로그인 성공 시 파싱용 변수
    var token: String,
    var user: UserData,

//    이 밑으로는 약속 목록 파싱용 변수
    var appointments: List<AppointmentData>
) {
}

