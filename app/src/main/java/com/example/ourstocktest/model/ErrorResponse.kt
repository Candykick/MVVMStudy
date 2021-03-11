package com.example.ourstocktest.model

import com.example.ourstocktest.data.service.API_CALL_ERROR_MESSAGE
import com.example.ourstocktest.data.service.API_CONNECT_ERROR_CODE
import com.example.ourstocktest.data.service.NO_INTERNET_ERROR_CODE
import com.example.ourstocktest.data.service.NO_INTERNET_ERROR_MESSAGE
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

/**
 * 실패 시 : STATUS CODE != 200, 아래와 같은 JSON이 넘어옴.
    {
        "timestamp": "2021-02-17T05:31:45.125+00:00",
        "status": 409,
        "error": "Conflict",
        "message": "",
        "path": "/register"
    }
*/

data class ErrorResponse(
    @SerializedName("timestamp") val timestamp: String,
    @SerializedName("status") val status: Int,
    @SerializedName("error") val error: String,
    @SerializedName("message") val message: String,
    @SerializedName("path") val path: String
)

fun makeErrorResponseFromStatusCode(status: Int, path: String): ErrorResponse {
    return when(status) {
        204 -> ErrorResponse(SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()), status, "No Content", "해당 정보는 존재하지 않습니다.", path)
        400 -> ErrorResponse(SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()), status, "Bad Request", "잘못된 요청입니다.", path)
        401 -> ErrorResponse(SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()), status, "Unauthorized", "인증 정보가 잘못되었습니다.", path)
        403 -> ErrorResponse(SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()), status, "Forbidden", "잘못된 요청입니다.", path)
        404 -> ErrorResponse(SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()), status, "Not Found", "해당 주소를 찾을 수 없습니다.", path)
        406 -> ErrorResponse(SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()), status, "Not Acceptable", "잘못된 요청입니다.", path)
        408 -> ErrorResponse(SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()), status, "Request Timeout", "요청 시간이 초과되었습니다.", path)
        410 -> ErrorResponse(SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()), status, "Gone", "요청하신 API는 삭제되어 사용이 불가능합니다.", path)
        500 -> ErrorResponse(SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()), status, "Internal Server Error", "서버에서 오류가 발생했습니다.", path)
        502 -> ErrorResponse(SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()), status, "Bad Gateway", "게이트웨이가 연결된 서버로부터 잘못된 응답을 받았습니다.", path)
        503 -> ErrorResponse(SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()), status, "Service Temporarily Unavailable", "현재 서비스는 일시적으로 사용이 불가능한 상태입니다. 잠시 후에 다시 시도해 주세요.", path)
        504 -> ErrorResponse(SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()), status, "Gateway Timeout", "게이트웨이가 연결된 서버로부터 응답을 받을 수 없습니다.", path)
        NO_INTERNET_ERROR_CODE -> ErrorResponse(SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()), status, "No Internet", NO_INTERNET_ERROR_MESSAGE, path)
        else -> ErrorResponse(SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()), API_CONNECT_ERROR_CODE, API_CALL_ERROR_MESSAGE, API_CALL_ERROR_MESSAGE, path)
    }
}