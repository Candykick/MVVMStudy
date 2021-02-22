package com.example.ourstocktest.di

import com.example.ourstocktest.data.remote.RemoteUserSource
import com.example.ourstocktest.data.remote.RemoteUserSourceImpl
import org.koin.dsl.module

val remoteSourceModule = module {
    single<RemoteUserSource> { RemoteUserSourceImpl(service = get()) }
}