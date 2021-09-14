package com.example.databinding20210910.datas

import com.google.gson.annotations.SerializedName
import org.json.JSONObject
import java.io.Serializable

class UserData (
    var id: Int,
    var provider: String,
    var uid: String,
    var email: String,
    @SerializedName("nick_name")
    var nickName: String,
    @SerializedName("ready_minute")
    var readyMinute: Int) : Serializable {

//    constructor() : this(0, "", "", "", "", "", "")


    companion object{



    }


}