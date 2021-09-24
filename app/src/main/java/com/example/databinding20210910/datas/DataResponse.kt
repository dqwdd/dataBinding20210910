package com.example.databinding20210910.datas

class DataResponse(
//    로그인 성공 시 파싱용 변수
    var user: UserData,
    var token: String,

//    이 밑으로는 약속 목록 파싱용 변수
    var appointments: List<AppointmentData>,
    var invited_appointments: List<AppointmentData>,

    //장소 목록 파싱
    var places : List<PlaceData>,

    //친구 목록
    var friends : List<UserData>,

    //검색 목록
    var users : List<UserData>,//json이랑 이름 같게 해야 함

    //하나의 상세 약속 정보
    var appointment: AppointmentData
) {
}

