package com.example.ourstocktest.di

import android.content.Context
import com.example.ourstocktest.dialog.LoadingDialog
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val loadingModule = module {
    single {(context: Context) ->
        LoadingDialog(context)
    }
}