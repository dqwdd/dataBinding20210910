package com.example.databinding20210910.datas

import com.google.gson.annotations.SerializedName

class AppointmentData(
    var id: Int,
    @SerializedName("user_id")
    var userId: Int,
    var title: String,
    var datetime: String, //일단 String -> 파싱 기능 수정 => Date형태로 받자(Calendar와 엮어서 사용)
    @SerializedName("place")//서버에서 받는 이름은 이건데 앱에서 쓸 이름은 아래꺼로 할래
    var placeName: String,
    var latitude: Double,
    var longitude: Double,
    @SerializedName("created_at")
    var createdAt: String,
    var user: UserData
) {
}