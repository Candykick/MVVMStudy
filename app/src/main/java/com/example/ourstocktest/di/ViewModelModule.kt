package com.example.ourstocktest.di

import android.app.Activity
import android.content.Context
import com.example.ourstocktest.ClauseViewModel
import com.example.ourstocktest.HomeViewModel
import com.example.ourstocktest.SocketViewModel
import com.example.ourstocktest.ViewModel
import com.example.ourstocktest.channel.ChannelInfoViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { (activity: Activity) -> ViewModel(repository = get(), activity = activity) }
    viewModel { ClauseViewModel(repository = get()) }
    viewModel { SocketViewModel() }
    viewModel { HomeViewModel(repository = get()) }
    viewModel { ChannelInfoViewModel(repository = get()) }
}