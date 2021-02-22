package com.example.ourstocktest.data.remote

import com.example.ourstocktest.data.service.Api
import com.example.ourstocktest.model.UserAlarmUpdateRequest
import com.example.ourstocktest.model.UserFcmSaveRequest
import com.example.ourstocktest.model.UserLoginRequest
import com.example.ourstocktest.model.UserRegisterRequest
import retrofit2.Call

interface RemoteUserSource {
    suspend fun registerUserApi(user: UserRegisterRequest): String
    suspend fun loginUserApi(phonenumber: UserLoginRequest): String
    suspend fun saveFcmUserApi(userInfoToken: UserFcmSaveRequest): String
    suspend fun alarmUpdateUserApi(userAlarmType: UserAlarmUpdateRequest): String
}

class RemoteUserSourceImpl(private val service: Api) : RemoteUserSource {
    override suspend fun registerUserApi(user: UserRegisterRequest) = service.registerUserApi(user)
    override suspend fun loginUserApi(phonenumber: UserLoginRequest) = service.loginUserApi(phonenumber)
    override suspend fun saveFcmUserApi(userInfoToken: UserFcmSaveRequest) = service.saveFcmUserApi(userInfoToken)
    override suspend fun alarmUpdateUserApi(userAlarmType: UserAlarmUpdateRequest) = service.alarmUpdateUserApi(userAlarmType)
}