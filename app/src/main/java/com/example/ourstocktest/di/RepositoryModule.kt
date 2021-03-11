package com.example.ourstocktest.di

import android.app.Activity
import com.example.ourstocktest.data.repository.*
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthProvider
import org.koin.dsl.module

val repositoryModule = module {
    single<UserRepository> { UserRepositoryImpl(remoteUserSource = get()) }
    single<CertificationRepository> {
        (callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks, success: (m: FirebaseUser?) -> Unit,
                failed: (m: String) -> Unit, activity: Activity) ->
        CertificationRepositoryImpl(callbacks = callbacks, success = success, failed = failed, activity = activity)
    }
    single<ClauseRepository> { ClauseRepositoryImpl(remoteClauseSource = get()) }
    single<MainRepository> { MainRepositoryImpl(remoteMainSource = get()) }
    single<ChannelRepository> { ChannelRepositoryImpl(channelRemoteSource = get()) }
}