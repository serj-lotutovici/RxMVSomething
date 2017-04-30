package com.serjltt.devfest.weather.data

import com.serjltt.devfest.weather.data.network.NetworkModule
import com.serjltt.devfest.weather.di.Global
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Global @Module(includes = arrayOf(NetworkModule::class)) class DataModule {
  interface Component {
    fun weatherService(): WeatherService
  }

  @Provides internal fun provideWeatherService(retrofit: Retrofit): WeatherService =
      retrofit.create(WeatherService::class.java)
}
