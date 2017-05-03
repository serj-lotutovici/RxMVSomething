package com.serjltt.devfest.weather.show.forecast.usecase

import com.serjltt.devfest.weather.cast
import com.serjltt.devfest.weather.data.WeatherService
import com.serjltt.devfest.weather.data.network.NetworkTestRule
import com.serjltt.devfest.weather.fromResource
import com.serjltt.devfest.weather.rx.ImmediateTestScheduler
import com.serjltt.devfest.weather.show.forecast.model.ForecastData
import com.serjltt.devfest.weather.show.forecast.model.ForecastModel
import okhttp3.mockwebserver.MockResponse
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@Suppress("IllegalIdentifier")
class GetForecastUseCaseTest {
  @get:Rule var testRule = NetworkTestRule()

  private lateinit var useCase: GetForecastUseCase

  @Before fun setUp() {
    useCase = GetForecastUseCaseImpl(testRule.create(WeatherService::class),
        ImmediateTestScheduler())
  }

  @Test fun `UseCase propagates progress and success events`() {
    testRule.enqueue(MockResponse().setBody(fromResource("yahoo_response.json")))

    val values = useCase.getForecast("any")
        .test()
        .assertNoErrors()
        .assertValueCount(2)
        .values()

    assertThat(values.first()).isEqualTo(ForecastModel.Progress)
    values.last().cast<ForecastModel.Success>().let { (data) ->
      assertThat(data).hasSize(10)
      assertThat(data[9])
          .isEqualToComparingFieldByField(ForecastData("08 Aug 2016", "59", "70"))
    }
  }

  @Test fun `UseCase propagates progress and error events`() {
    testRule.enqueue(MockResponse().setResponseCode(400))

    val values = useCase.getForecast("any")
        .test()
        .assertNoErrors()
        .assertValueCount(2)
        .values()

    assertThat(values.first()).isEqualTo(ForecastModel.Progress)
    values.last().cast<ForecastModel.Error>().let { (throwable) ->
      assertThat(throwable).hasMessage("HTTP 400 Client Error")
    }
  }
}
