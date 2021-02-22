package com.example.ourstocktest.model

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
    val timestamp: String,
    val status: Int,
    val error: String,
    val message: String,
    val path: String
)