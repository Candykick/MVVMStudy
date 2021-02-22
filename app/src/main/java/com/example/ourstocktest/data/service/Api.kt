package com.example.ourstocktest.data.service

import com.example.ourstocktest.model.UserAlarmUpdateRequest
import com.example.ourstocktest.model.UserFcmSaveRequest
import com.example.ourstocktest.model.UserLoginRequest
import com.example.ourstocktest.model.UserRegisterRequest
import retrofit2.Call
import retrofit2.http.Body
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
    suspend fun loginUserApi(@Body phonenumber: UserLoginRequest): String

    @POST("user/fcm")
    suspend fun saveFcmUserApi(@Body userInfoToken: UserFcmSaveRequest): String//Call<String>

    @POST("user/alarm")
    suspend fun alarmUpdateUserApi(@Body userAlarmType: UserAlarmUpdateRequest): String
}

/** 모든 Repository가 사용할 응답용 콜백. */
interface ApiCallback<T> {
    fun onSuccess(item: T)
    fun onFailed(result: String)
}
// ApiCallback에서 onFailed에 해당하는 메세지가 없을 경우 출력하는 메세지
const val API_CALL_ERROR_MESSAGE = "알 수 없는 오류가 발생했습니다."