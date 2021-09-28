package com.example.databinding20210910.web

import com.example.databinding20210910.datas.BasicResponse
import okhttp3.MultipartBody
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
        @Field("start_place") startPlaceName: String,
        @Field("start_latitude") startLat: Double,
        @Field("start_longitude") startLng: Double,
        @Field("place") placeName: String,
        @Field("latitude") lat: Double,
        @Field("longitude") lng: Double,
        @Field("friend_list") list : String) : Call<BasicResponse>


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




    @FormUrlEncoded
    @PATCH("/user/place")
    fun postRequestAddMyPlace(
        @Field("name") name:String,
        @Field("latitude") lat :Double,
        @Field("longitude") lng:Double,
        @Field("is_primary") isPrimary:Boolean) : Call<BasicResponse>




    @GET("/user/place")
    fun getRequestMyPlaceList() : Call<BasicResponse>



    //프로필사진 첨부 => 사진첨부니까 Multipart활용
    //Multipart 방식의 통신에서는 Field를 담지 않고, MultipartBody.Part 양식으로(파일)데이터 첨부
    //사진 외의 데이터도 첨부할 때는(글이나 뭐 그런거), 나머지 항목들은 RequestBody 형태로 첨부함
    @Multipart
    @PUT("/user/image")
    fun putRequestProfileImg(@Part profileImg : MultipartBody.Part) : Call<BasicResponse>




    //친구목록 불러오기
    //쿼리 파라미터를 넣어서 불러오기
    @GET("/user/friend")
    fun getRequestFriendList(@Query("type") type: String) : Call<BasicResponse>



    //유저 검색하기(닉네임으로)
    @GET("/search/user")
    fun getRequestSearchUser(@Query("nickname") type: String) : Call<BasicResponse>




    //친구추가 요청 날리기
    @FormUrlEncoded
    @POST("/user/friend")
    fun postRequestAddFriend(
        @Field("user_id") userId: Int ) : Call<BasicResponse>




    //친구추가 요청 수락/거절 하기
    @FormUrlEncoded
    @PUT("/user/friend")//어느 주소로 갈거냐
    fun putRequestRequestReply(
        @Field("user_id") userId: Int,
        @Field("type") type: String) : Call<BasicResponse>





    @FormUrlEncoded
    @POST("/appointment/arrival")
    fun postRequestArrival(
        @Field("appointment_id") id: Int,
        @Field("latitude") lat: Double,
        @Field("longitude") lnt: Double ) : Call<BasicResponse>





    @GET("/appointment/{appointment_id}")
    fun getRequestAppointmentDetail(
        @Path("appointment_id") id: Int//여기있는 변수를 받아서 위에 {appointment_id}에 넣는다!
    ) : Call<BasicResponse>





    //알림 목록 50개 조회
    @GET("/notifications")
    fun getRequestNotifications(
        @Query("need_all_notis") notice: String) : Call<BasicResponse>




    //알림 읽음 처리
    @FormUrlEncoded
    @POST("/notifications")
    fun postRequestNotificatioRead(
        @Field("noti_id") notiId: Int) : Call<BasicResponse>




    //약속 삭제 처리
    @DELETE("/appointment")
    fun getRequestAppointmentDelete(
        @Query("appointment_id") appointmentId: Int) : Call<BasicResponse>




    //비밀번호 변경
    @FormUrlEncoded
    @PATCH("/user/password")
    fun getRequestUserPasswordChange(
        @Field("current_password") currentPassword : String,
        @Field("new_password") newPassword  : String) : Call<BasicResponse>





    //약속 삭제 처리
    @DELETE("/user/image")
    fun getRequestProfileImageDelete() : Call<BasicResponse>






    // 아이디/닉네임 중복 검사
    @GET("/user/check")
    fun getRequestIDNicknameCheck(
        @Query("type") type: String,
        @Query("value") value: String) : Call<BasicResponse>





    //약속 수정하기
    @FormUrlEncoded
    @PUT("/appointment")//어느 주소로 갈거냐
    fun putRequestModifyAppointment(
        @Field("appointment_id") appointmentId : Int,//
        @Field("title") title : String,//
        @Field("datetime") dateTime: String,//
        @Field("place") place: String,//
        @Field("latitude") latitude : Double,//
        @Field("longitude") longitude : Double,//
        @Field("friend_list") friendList: String
    ) : Call<BasicResponse>

}