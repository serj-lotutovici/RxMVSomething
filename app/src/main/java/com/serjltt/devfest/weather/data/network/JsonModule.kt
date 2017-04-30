package com.serjltt.devfest.weather.data.network

import com.serjltt.devfest.weather.di.Global
import com.serjltt.moshi.adapters.WrappedJsonAdapter
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides

@Global @Module internal class JsonModule {
  @Global @Provides internal fun provideMoshi(): Moshi {
    return Moshi.Builder()
        .add(WrappedJsonAdapter.FACTORY)
        .build()
  }
}
