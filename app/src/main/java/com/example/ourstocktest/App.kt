package com.example.ourstocktest

import android.app.Application
import com.example.ourstocktest.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(networkModule, remoteSourceModule, repositoryModule, viewModelModule)
            modules(loadingModule)
        }
    }

}