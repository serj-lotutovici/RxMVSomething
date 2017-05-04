package com.serjltt.devfest.weather.data.network

import android.content.Context
import com.serjltt.devfest.weather.BuildConfig
import com.serjltt.devfest.weather.di.Global
import com.serjltt.devfest.weather.rx.RxModule
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Named

@Global @Module(includes = arrayOf(JsonModule::class))
open class NetworkModule(internal val endpoint: HttpUrl) {
  @Global @Provides internal fun provideRetrofit(client: OkHttpClient, moshi: Moshi,
      @Named(RxModule.IO_SCHEDULER) ioScheduler: Scheduler): Retrofit =
      Retrofit.Builder()
          .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(ioScheduler))
          .addConverterFactory(MoshiConverterFactory.create(moshi))
          .client(client)
          .baseUrl(endpoint)
          .build()

  @Global @Provides internal fun provideOkHttpClient(cache: Cache?,
      @Named(NAME_LOGGING) loggingInterceptor: Interceptor,
      @Named(NAME_CACHE) cacheInterceptor: Interceptor,
      @Named(NAME_OFFLINE_CACHE) offlineCacheInterceptor: Interceptor): OkHttpClient =
      OkHttpClient.Builder()
          .cache(cache)
          .addInterceptor(loggingInterceptor)
          .addInterceptor(offlineCacheInterceptor)
          .addNetworkInterceptor(cacheInterceptor)
          .build()

  @Global @Provides internal fun provideCache(context: Context): Cache =
      Cache(File(context.cacheDir, "http-cache"), HTTP_CACHE_SIZE.toLong())

  @Global @Provides @Named(NAME_LOGGING) internal open fun provideLoggingInterceptor()
      : Interceptor =
      HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
          HttpLoggingInterceptor.Level.BODY
        } else {
          HttpLoggingInterceptor.Level.NONE
        }
      }

  @Global @Provides @Named(NAME_CACHE) internal open fun provideCacheInterceptor(): Interceptor =
      Interceptor { chain ->
        val response = chain.proceed(chain.request())
        // re-write response header to force use of cache
        val cacheControl = CacheControl.Builder()
            .maxAge(1, TimeUnit.MINUTES)
            .build()

        response.newBuilder()
            .header(HEADER_CACHE_CONTROL, cacheControl.toString())
            .build()
      }


  @Global @Provides @Named(NAME_OFFLINE_CACHE) internal open fun provideOfflineCacheInterceptor(
      deviceNetwork: DeviceNetwork): Interceptor =
      Interceptor { chain ->
        var request = chain.request()
        if (!deviceNetwork.isNetworkAvailable()) {
          // If no network is available we use the before returned
          // response which is not older than 7 days.
          val cacheControl = CacheControl.Builder().maxStale(1, TimeUnit.HOURS).build()
          request = request.newBuilder().cacheControl(cacheControl).build()
        }
        chain.proceed(request)
      }

  @Global @Provides internal fun provideNetwork(context: Context): DeviceNetwork =
      RealDeviceNetwork(context)

  companion object {
    internal const val HEADER_CACHE_CONTROL = "Cache-Control"
    internal const val NAME_LOGGING = "logging"
    internal const val NAME_CACHE = "cache"
    internal const val NAME_OFFLINE_CACHE = "offline_cache"
    internal const val ONE_KILOBYTE = 1024
    internal const val HTTP_CACHE_SIZE = 10 * ONE_KILOBYTE * ONE_KILOBYTE
  }
}
