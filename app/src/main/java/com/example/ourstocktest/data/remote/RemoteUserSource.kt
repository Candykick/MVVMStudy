package com.example.ourstocktest.data.remote

import com.example.ourstocktest.data.service.Api
import com.example.ourstocktest.model.*
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Response

interface RemoteUserSource {
    suspend fun registerUserApi(user: UserRegisterRequest): String
    suspend fun loginUserApi(phonenumber: UserLoginRequest): UserLoginResponse
    suspend fun saveFcmUserApi(userInfoToken: UserFcmSaveRequest): String
    suspend fun alarmUpdateUserApi(userAlarmType: UserAlarmUpdateRequest): String
}

class RemoteUserSourceImpl(private val service: Api) : RemoteUserSource {
    override suspend fun registerUserApi(user: UserRegisterRequest) = service.registerUserApi(user)
    override suspend fun loginUserApi(phonenumber: UserLoginRequest) = service.loginUserApi(phonenumber)
    override suspend fun saveFcmUserApi(userInfoToken: UserFcmSaveRequest) = service.saveFcmUserApi(userInfoToken)
    override suspend fun alarmUpdateUserApi(userAlarmType: UserAlarmUpdateRequest) = service.alarmUpdateUserApi(userAlarmType)
}