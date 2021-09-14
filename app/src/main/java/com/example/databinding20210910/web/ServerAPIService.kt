package com.example.databinding20210910.web

import com.example.databinding20210910.datas.BasicResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.*

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


    @FormUrlEncoded
    @POST("/user/social")//어느 주소로 갈거냐
    fun postRequestSocialLogin(
        @Field("provider") provider: String,
        @Field("uid") id: String,
        @Field("nick_name") name: String) : Call<BasicResponse>


    @FormUrlEncoded
    @POST("/appointment")//어느 주소로 갈거냐
    fun postRequestAppointment(
//        @Header("X-Http-Token") token: String,   -> 달아주는 코딩(interceptor)만들어서 안써도 댐
        @Field("title") title: String,
        @Field("datetime") datetime: String,
        @Field("place") place: String,
        @Field("latitude") lat: Double,
        @Field("longitude") lng: Double, ) : Call<BasicResponse>


//    GET - 약속 목록 가져오기

    @GET("/appointment")
    fun getRequestAppointmentList() : Call<BasicResponse>

    @GET("/user")
    fun getRequestMyInfo() : Call<BasicResponse>


//    POST - PUT - PATCH : FormData 활용
      // 회원 정보 수정 API
    @FormUrlEncoded
    @PATCH("/user")
    fun patchRequestMyInfo(
        @Field("field") field:String,
        @Field("value") value:String ) : Call<BasicResponse>





}