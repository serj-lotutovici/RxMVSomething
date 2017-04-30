package com.serjltt.devfest.weather.data.network

import okhttp3.HttpUrl
import okhttp3.Interceptor

/** Allows us to hook in different interceptors for testing purposes.  */
class TestNetworkModule(endpoint: HttpUrl) : NetworkModule(endpoint) {
  val urlSwappingInterceptor: UrlSwappingInterceptor = UrlSwappingInterceptor(endpoint)

  // We replace the logging interceptor with a url swapping one
  // feels bad to alter the dependency graph, but Retrofit doesn't give us much choice.
  override fun provideLoggingInterceptor(): Interceptor {
    return urlSwappingInterceptor
  }

  override fun provideCacheInterceptor(): Interceptor {
    return dummyInterceptor()
  }

  override fun provideOfflineCacheInterceptor(deviceNetwork: DeviceNetwork): Interceptor {
    return dummyInterceptor()
  }

  /** Allows to override the caching interceptors, so that tests would run independently.  */
  private fun dummyInterceptor(): Interceptor {
    return Interceptor { chain -> chain.proceed(chain.request()) }
  }
}
