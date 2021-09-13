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
    var created_at: String,
    var updated_at: String,) : Serializable {

//    constructor() : this(0, "", "", "", "", "", "")


    companion object{



    }


}