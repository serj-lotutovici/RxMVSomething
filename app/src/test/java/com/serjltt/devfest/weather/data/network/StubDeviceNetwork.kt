package com.serjltt.devfest.weather.data.network


class StubDeviceNetwork(var stubState: Long = DeviceNetwork.ON_WIFI) : DeviceNetwork {
  override fun getCurrentNetworkState(): Long = stubState
}
