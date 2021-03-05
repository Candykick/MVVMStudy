package com.example.ourstocktest.di

import android.app.Activity
import android.content.Context
import com.example.ourstocktest.ClauseViewModel
import com.example.ourstocktest.ViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { (activity: Activity) -> ViewModel(repository = get(), activity = activity) }
    viewModel  { ClauseViewModel(repository = get()) }
}