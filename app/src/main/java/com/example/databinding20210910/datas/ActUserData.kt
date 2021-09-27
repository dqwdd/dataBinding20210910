package com.example.databinding20210910.datas

data class ActUserData(
    val created_at: String,
    val email: String,
    val id: Int,
    val nick_name: String,
    val profile_img: String,
    val provider: String,
    val ready_minute: Int,
    val uid: Any,
    val updated_at: String
)