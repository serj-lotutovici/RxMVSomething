package com.serjltt.devfest.weather.di

import android.content.Context
import dagger.Module
import dagger.Provides

@Global @Module class PlatformModule(internal val context: Context) {
  @Provides internal fun provideContext(): Context = context
}
