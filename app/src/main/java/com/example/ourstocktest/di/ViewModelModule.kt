package com.example.ourstocktest.di

import com.example.ourstocktest.ViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { ViewModel(repository = get()) }
}