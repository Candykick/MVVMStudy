package com.example.ourstocktest.data.repository

import com.example.ourstocktest.data.remote.RemoteUserSource
import com.example.ourstocktest.data.service.Api
import com.example.ourstocktest.model.*
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Response

interface UserRepository {
    suspend fun register(callNumber: String, telecom: String, username: String,
                         residentRegistrationNumberFront: String, residentRegistrationNumberBack: String) : String

    suspend fun login(phone: String): UserLoginResponse

    suspend fun saveFcm(userId: Int, fcmToken: String): String

    suspend fun alarmUpdate(userId: Int, alarm: Int, marketing: Int): String
}

class UserRepositoryImpl(private val remoteUserSource: RemoteUserSource) : UserRepository {
    override suspend fun register(callNumber: String, telecom: String, username: String,
        residentRegistrationNumberFront: String, residentRegistrationNumberBack: String)
            = remoteUserSource.registerUserApi(
        UserRegisterRequest(callNumber, telecom, username,
        residentRegistrationNumberFront, residentRegistrationNumberBack)
    )

    override suspend fun login(phone: String) = remoteUserSource.loginUserApi(UserLoginRequest(phone)) /*{//ApiResponse<UserLoginResponse> {
        val apiResult: Response<String> = remoteUserSource.loginUserApi(UserLoginRequest(phone))

        return if(apiResult.code() == 200) {
            ApiResponse(true, Gson().fromJson<UserLoginResponse>(apiResult.body(), UserLoginResponse::class.java), null)
        } else {
            ApiResponse(false, null, Gson().fromJson<ErrorResponse>(apiResult.body(), ErrorResponse::class.java))
        }
    }*/

    override suspend fun saveFcm(userId: Int, fcmToken: String) = remoteUserSource.saveFcmUserApi(
        UserFcmSaveRequest(userId, fcmToken)
    )

    override suspend fun alarmUpdate(userId: Int, alarm: Int, marketing: Int) = remoteUserSource.alarmUpdateUserApi(
        UserAlarmUpdateRequest(userId, alarm, marketing)
    )
}

/*fun registerUser(callNumber: String, telecom: String, username: String, residentRegistrationNumberFront: String,
                 residentRegistrationNumberBack: String, callback: ApiCallback<UserRegisterResponse>
) {


    Api.create().registerUserApi(requestData).enqueue(object: Callback<String> {
        override fun onResponse(call: Call<String>, response: Response<String>) {
            if(response.code() != 200) {
                // 오류가 있다면
                if(response.body() != null && response.body() != "") {
                    val error: ErrorResponse = Api.gson.fromJson(response.body()!!, ErrorResponse::class.java)
                    callback.onFailed(error.message)
                } else {
                    callback.onFailed(API_CALL_ERROR_MESSAGE)
                }
            } else {
                // 오류가 없다면
                if(response.body() != null && response.body() != "") {
                    val responseData: UserRegisterResponse = Api.gson.fromJson(response.body()!!, UserRegisterResponse::class.java)
                    callback.onSuccess(responseData)
                } else {
                    callback.onFailed(API_CALL_ERROR_MESSAGE)
                }
            }
        }

        override fun onFailure(call: Call<String>, t: Throwable) {
            if(t.localizedMessage != null)
                callback.onFailed(t.localizedMessage!!)
            else
                callback.onFailed(API_CALL_ERROR_MESSAGE)
        }
    })
}

fun loginUser(phone: String, callback: ApiCallback<UserLoginResponse>) {
    val requestData = UserLoginRequest(phone)

    Api.create().loginUserApi(requestData).enqueue(object: Callback<String> {
        override fun onResponse(call: Call<String>, response: Response<String>) {
            if(response.code() != 200) {
                // 오류가 있다면
                if(response.body() != null && response.body() != "") {
                    val error: ErrorResponse = Api.gson.fromJson(response.body()!!, ErrorResponse::class.java)
                    callback.onFailed(error.message)
                } else {
                    callback.onFailed(API_CALL_ERROR_MESSAGE)
                }
            } else {
                // 오류가 없다면
                if(response.body() != null && response.body() != "") {
                    val responseData: UserLoginResponse = Api.gson.fromJson(response.body()!!, UserLoginResponse::class.java)
                    callback.onSuccess(responseData)
                } else {
                    callback.onFailed(API_CALL_ERROR_MESSAGE)
                }
            }
        }

        override fun onFailure(call: Call<String>, t: Throwable) {
            if(t.localizedMessage != null)
                callback.onFailed(t.localizedMessage!!)
            else
                callback.onFailed(API_CALL_ERROR_MESSAGE)
        }
    })
}

fun saveFcmUser(userId: Int, fcmToken: String, callback: ApiCallback<UserFcmSaveResponse>) {
    val requestData =
        UserFcmSaveRequest(userId, fcmToken)

    Api.create().saveFcmUserApi(requestData).enqueue(object: Callback<String> {
        override fun onResponse(call: Call<String>, response: Response<String>) {
            if(response.code() != 200) {
                // 오류가 있다면
                if(response.body() != null && response.body() != "") {
                    val error: ErrorResponse = Api.gson.fromJson(response.body()!!, ErrorResponse::class.java)
                    callback.onFailed(error.message)
                } else {
                    callback.onFailed(API_CALL_ERROR_MESSAGE)
                }
            } else {
                // 오류가 없다면
                if(response.body() != null && response.body() != "") {
                    val responseData: UserFcmSaveResponse = Api.gson.fromJson(response.body()!!, UserFcmSaveResponse::class.java)
                    callback.onSuccess(responseData)
                } else {
                    callback.onFailed(API_CALL_ERROR_MESSAGE)
                }
            }
        }

        override fun onFailure(call: Call<String>, t: Throwable) {
            if(t.localizedMessage != null)
                callback.onFailed(t.localizedMessage!!)
            else
                callback.onFailed(API_CALL_ERROR_MESSAGE)
        }
    })
}

fun alarmUpdateUser(userId: Int, alarm: Int, marketing: Int, callback: ApiCallback<UserAlarmUpdateResponse>) {
    val requestData = UserAlarmUpdateRequest(
        userId,
        alarm,
        marketing
    )

    Api.create().alarmUpdateUserApi(requestData).enqueue(object: Callback<String> {
        override fun onResponse(call: Call<String>, response: Response<String>) {
            if(response.code() != 200) {
                // 오류가 있다면
                if(response.body() != null && response.body() != "") {
                    val error: ErrorResponse = Api.gson.fromJson(response.body()!!, ErrorResponse::class.java)
                    callback.onFailed(error.message)
                } else {
                    callback.onFailed(API_CALL_ERROR_MESSAGE)
                }
            } else {
                // 오류가 없다면
                if(response.body() != null && response.body() != "") {
                    val responseData: UserAlarmUpdateResponse = Api.gson.fromJson(response.body()!!, UserAlarmUpdateResponse::class.java)
                    callback.onSuccess(responseData)
                } else {
                    callback.onFailed(API_CALL_ERROR_MESSAGE)
                }
            }
        }

        override fun onFailure(call: Call<String>, t: Throwable) {
            if(t.localizedMessage != null)
                callback.onFailed(t.localizedMessage!!)
            else
                callback.onFailed(API_CALL_ERROR_MESSAGE)
        }
    })
}*/