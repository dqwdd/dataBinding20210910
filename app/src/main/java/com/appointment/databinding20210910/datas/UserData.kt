package com.appointment.databinding20210910.datas

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

class UserData (
    var id: Int,
    var provider: String,
    @SerializedName("nick_name")
    var nickName: String,
    var email: String,
    @SerializedName("profile_img")
    var profileImgURL: String,
    @SerializedName("ready_minute")
    var readyMinute: Int,
    @SerializedName("arrived_at")
    var arrivedAt : Date?) : Serializable {
}