package com.serjltt.devfest.weather.data.network

import android.support.annotation.IntDef

@IntDef(*longArrayOf(DeviceNetwork.OFFLINE, DeviceNetwork.ON_WIFI, DeviceNetwork.ON_MOBILE))
@Retention(AnnotationRetention.SOURCE) annotation class NetworkState
