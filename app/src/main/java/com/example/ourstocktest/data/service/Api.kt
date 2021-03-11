package com.example.ourstocktest.data.service

import com.example.ourstocktest.model.*
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/* 공부 중인 내용들 정리
 * 1. MVVM 패턴
 *    https://medium.com/hongbeomi-dev/aac를-사용하여-mvvm-pattern을-구현한-안드로이드-앱-만들기-1d6d73689bd0
 *    https://medium.com/@jsuch2362/android-에서-mvvm-으로-긴-여정을-82494151f312
 *    https://dev-imaec.tistory.com/37
 */

/** API 응답 양식 : 2020.02.17 (수)
 *
 *  성공 시 : STATUS CODE == 200, http://service.ourstock.ga:9090/swagger-ui.html#!/user-controller/registerUserUsingPOST 에 있는 방식으로 처리함.
 *
 *  실패 시 : STATUS CODE != 200, 아래와 같은 JSON이 넘어옴.
 *          {
                "timestamp": "2021-02-17T05:31:45.125+00:00",
                "status": 409,
                "error": "Conflict",
                "message": "",
                "path": "/register"
            }

 * 이 때문에, 모든 응답은 우선 String으로 받은 후, STATUS CODE에 따라 알맞은 JSON OBJECT로 변환해서 처리한다.
 */

interface Api {
    @POST("register")
    suspend fun registerUserApi(@Body user: UserRegisterRequest): String

    @POST("login")
    suspend fun loginUserApi(@Body phonenumber: UserLoginRequest): UserLoginResponse//Response<String>

    @POST("user/fcm")
    suspend fun saveFcmUserApi(@Body userInfoToken: UserFcmSaveRequest): String//Call<String>

    @POST("user/alarm")
    suspend fun alarmUpdateUserApi(@Body userAlarmType: UserAlarmUpdateRequest): String

    @GET("channelList")
    suspend fun channelListApi(): Response<String>

    @GET("channelInfo")
    suspend fun channelInfoApi(@Body channelId: ChannelIdRequest): Response<String>

    @GET("channelLiveChart")
    suspend fun channelLiveChartApi(@Body channelId: ChannelIdRequest): Response<String>

    @GET("channelDayChart")
    suspend fun channelDayChartApi(@Body channelId: ChannelIdRequest): Response<String>
}

// ApiCallback에서 onFailed에 해당하는 메세지가 없을 경우 출력하는 메세지
const val API_CALL_ERROR_MESSAGE = "알 수 없는 오류가 발생했습니다."
// 인터넷 없음 오류
const val NO_INTERNET_ERROR_MESSAGE = "인터넷 연결이 불안정합니다. 다시 시도해 주세요."

// API 접속 과정에서 오류가 발생한 경우 Status Code. (즉 인터넷 연결이 끊기거나, 서버가 죽은 경우 등의 이유로 인해, 서버로부터 응답도 받지 못한 상태로 발생한 오류.)
const val API_CONNECT_ERROR_CODE = 999
// 인터넷 없음 오류
const val NO_INTERNET_ERROR_CODE = 1395
