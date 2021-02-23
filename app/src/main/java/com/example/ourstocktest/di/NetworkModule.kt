package com.example.ourstocktest.di

import com.example.ourstocktest.data.service.Api
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val CONNECT_TIMEOUT = 30L
private const val WRITE_TIMEOUT = 30L
private const val READ_TIMEOUT = 30L
private const val BASE_URL = "http://service.ourstock.ga:9090/"

val networkModule = module {

    single {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        OkHttpClient.Builder().apply {
            addInterceptor(interceptor)
            connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            retryOnConnectionFailure(true)
        }.build()
    }

    single {
        Retrofit.Builder().client(get()).baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            //.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    }

    single { get<Retrofit>().create(Api::class.java) }
}