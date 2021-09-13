package com.example.databinding20210910.datas

import org.json.JSONObject
import java.io.Serializable

class UserData (
    var id: Int,
    var email: String,
    var nick_name: String,
    var created_at: String,
    var updated_at: String,) : Serializable {

    constructor() : this(0, "", "", "", "")


    companion object{



    }


}