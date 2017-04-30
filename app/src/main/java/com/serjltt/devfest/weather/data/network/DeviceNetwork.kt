package com.serjltt.devfest.weather.data.network

import android.support.annotation.WorkerThread

/** Allows the consumer to track the network state of the device.  */
interface DeviceNetwork {
  /** Returns the current network state of the device.  */
  @WorkerThread @NetworkState fun getCurrentNetworkState(): Long

  /** Checks network availability.  */
  @WorkerThread fun isNetworkAvailable(): Boolean =
      getCurrentNetworkState() != DeviceNetwork.OFFLINE

  companion object {
    const val OFFLINE = 1L
    const val ON_WIFI = 2L
    const val ON_MOBILE = 3L
  }
}
