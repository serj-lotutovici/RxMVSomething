package com.serjltt.devfest.weather.show.forecast

import android.content.Context
import android.content.SharedPreferences
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.serjltt.devfest.weather.di.Consumer
import dagger.Module
import dagger.Provides

@Consumer @Module class ForecastModule {
  @Provides internal fun provideSharedPreferences(context: Context) =
      context.getSharedPreferences("forecast", Context.MODE_PRIVATE)

  @Provides internal fun provideRxSharedPreferences(preferences: SharedPreferences) =
      RxSharedPreferences.create(preferences)

  @Provides internal fun provideCityPreference(rxPreferences: RxSharedPreferences) =
      rxPreferences.getString("city", "amsterdam")
}
