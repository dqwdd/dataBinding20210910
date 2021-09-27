package com.example.databinding20210910.datas

data class NotificationData(
    val act_user: ActUserData,
    val act_user_id: Int,
    val created_at: String,
    val focus_object_id: Int,
    val id: Int,
    val message: String,
    val receive_user_id: Int,
    val title: String,
    val type: String,
    val updated_at: String
)