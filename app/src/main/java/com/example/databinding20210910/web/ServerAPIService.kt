package com.example.databinding20210910.web

import com.example.databinding20210910.datas.BasicResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.PUT

interface ServerAPIService {


    @FormUrlEncoded
    @PUT("/user")//어느 주소로 갈거냐
    fun putRequestSignUp(
        @Field("email") email: String,
        @Field("password") pw: String,
        @Field("nick_name") nickname: String) : Call<BasicResponse>
    //폼데이터의 email에 뒤에 데이터들 담아주세요
    //Call<BasicResponse>==BasicResponse를 리턴해 줄꺼야


    @FormUrlEncoded
    @POST("/user")//어느 주소로 갈거냐
    fun putRequestSignIn(
        @Field("email") email: String,
        @Field("password") pw: String) : Call<BasicResponse>

}