package com.appointments.databinding20211110.web

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*

class DateDeserializer : JsonDeserializer<Date> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Date {

        val dateStr = json?.asString//josn을 string으로 바꿔줌

        //서버가 주는 양식으로 Date형태로 파싱
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss") //내가 지정하는거임""안을

        //서버가 준 String -> Date형식으로 변환 => 서버 기본값 : UTC 시간대 설정
        val resultDate = sdf.parse(dateStr)

        //UTC로 되어있는걸 내 핸드폰의 시차값 만큼 더해서 리턴
        val now = Calendar.getInstance()

        //결과로 나갈 시간 값의 밀리초단위로 => 시차 값 밀리초 단위 덧셈
        resultDate.time += now.timeZone.rawOffset//밀리초까지 계산이 된거

        //시차만큼 더해진 시간값이 파싱
        return resultDate

    }
}