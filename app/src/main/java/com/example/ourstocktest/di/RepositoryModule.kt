package com.example.ourstocktest.di

import com.example.ourstocktest.data.repository.UserRepository
import com.example.ourstocktest.data.repository.UserRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<UserRepository> { UserRepositoryImpl(remoteUserSource = get()) }
}