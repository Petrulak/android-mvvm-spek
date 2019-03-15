package com.petrulak.mvvm.di

import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.petrulak.mvvm.MvvmApp
import com.petrulak.mvvm.common.SchedulerProvider
import com.petrulak.mvvm.common.SchedulerProviderType
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
class MainModule(private val application: MvvmApp) {

    @Singleton
    @Provides
    fun provideApplication(): MvvmApp = application

    @Singleton
    @Provides
    fun provideSchedulerProvider(): SchedulerProviderType = SchedulerProvider()

    @Singleton
    @Provides
    fun provideGson(): Gson = Gson()

    @Singleton
    @Provides
    fun provideOkHttp(): OkHttpClient = OkHttpClient.Builder().build()

    @Singleton
    @Provides
    fun provideRetrofitOauth(client: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.coindesk.com/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }

}