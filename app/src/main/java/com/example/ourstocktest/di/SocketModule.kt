package com.example.ourstocktest.di

import io.socket.client.IO
import io.socket.client.Socket
import org.koin.dsl.module

private const val CONNECT_TIMEOUT = 30L
private const val WRITE_TIMEOUT = 30L
private const val READ_TIMEOUT = 30L
private const val BASE_URL = "wss://echo.websocket.org"

val socketModule = module {
    single { IO.socket(BASE_URL) }
}