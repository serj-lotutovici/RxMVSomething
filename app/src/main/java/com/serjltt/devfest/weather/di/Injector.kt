package com.serjltt.devfest.weather.di

import com.serjltt.devfest.weather.data.DataModule
import com.serjltt.devfest.weather.data.network.NetworkModule
import com.serjltt.devfest.weather.rx.RxModule
import dagger.Component

/** Applications global injector.  */
@Global
@Component(modules = arrayOf(
    PlatformModule::class,
    NetworkModule::class,
    DataModule::class,
    RxModule::class)
)
interface Injector
  : PlatformModule.Component,
    NetworkModule.Component,
    DataModule.Component,
    RxModule.Component
