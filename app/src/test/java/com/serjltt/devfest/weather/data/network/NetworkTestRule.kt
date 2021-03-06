package com.serjltt.devfest.weather.data.network

import com.serjltt.devfest.weather.rx.ImmediateTestScheduler
import io.reactivex.Scheduler
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.rules.RuleChain
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import retrofit2.Retrofit
import kotlin.reflect.KClass


class NetworkTestRule(
    val cache: Cache? = null,
    val deviceNetwork: DeviceNetwork = StubDeviceNetwork(),
    val ioScheduler: Scheduler = ImmediateTestScheduler())
  : TestRule {

  private val jsonTestRule = JsonTestRule()
  private val mockWebServer = MockWebServer()

  private val ruleChain: RuleChain = RuleChain.outerRule(mockWebServer)
      .around(jsonTestRule)

  private lateinit var client: OkHttpClient
  private lateinit var retrofit: Retrofit

  override fun apply(base: Statement, description: Description): Statement =
      ruleChain.apply(object : Statement() {
        override fun evaluate() {
          setupNetwork { module ->
            client = module.provideOkHttpClient(
                cache = cache,
                loggingInterceptor = module.provideLoggingInterceptor(),
                cacheInterceptor = module.provideCacheInterceptor(),
                offlineCacheInterceptor = module.provideOfflineCacheInterceptor(deviceNetwork)
            )
            retrofit = module.provideRetrofit(client, jsonTestRule.moshi, ioScheduler)
          }

          base.evaluate()
        }
      }, description)


  private inline fun setupNetwork(block: (NetworkModule) -> Unit) {
    block.invoke(NetworkModule(mockWebServer.url("/")))
  }

  fun <T : Any> create(service: KClass<T>): T = retrofit.create(service.java)
  fun enqueue(response: MockResponse) = mockWebServer.enqueue(response)
}
