package com.serjltt.devfest.weather.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.support.annotation.WorkerThread

internal class RealDeviceNetwork(val context: Context) : DeviceNetwork {
  @WorkerThread @NetworkState override fun getCurrentNetworkState(): Long {
    val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = manager.activeNetworkInfo

    // Determine the network state
    if (networkInfo == null || !networkInfo.isConnectedOrConnecting) {
      return DeviceNetwork.OFFLINE
    }

    if (networkInfo.type == ConnectivityManager.TYPE_WIFI) {
      return DeviceNetwork.ON_WIFI
    } else {
      return DeviceNetwork.ON_MOBILE
    }
  }
}
