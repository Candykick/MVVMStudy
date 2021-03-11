package com.example.ourstocktest.di

import com.example.ourstocktest.data.remote.*
import org.koin.dsl.module

val remoteSourceModule = module {
    single<RemoteUserSource> { RemoteUserSourceImpl(service = get()) }
    single<RemoteClauseSource> { RemoteClauseSourceImpl(service = get()) }
    single<RemoteMainSource> { RemoteMainSourceImpl(service = get()) }
    single<RemoteChannelSource> { RemoteChannelSourceImpl(service = get()) }
}