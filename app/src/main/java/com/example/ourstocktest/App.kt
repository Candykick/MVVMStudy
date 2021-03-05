package com.example.ourstocktest

import android.app.Application
import android.content.Context
import com.example.ourstocktest.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    companion object {
        lateinit var instance: App
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        startKoin {
            androidContext(this@App)
            modules(networkModule, remoteSourceModule, repositoryModule, viewModelModule)
            modules(loadingModule)
        }
    }

    fun context(): Context = applicationContext
}