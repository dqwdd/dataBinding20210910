package com.example.databinding20210910.web

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.PUT

interface ServerAPIService {


    @FormUrlEncoded
    @PUT("/user")//어느 주소로 갈거냐
    fun putRequestSignUp(
        @Field("email") email: String,
        @Field("password") pw: String,
        @Field("nick_name") nickname: String)//폼데이터의 email에 뒤에 데이터들 담아주세요

}