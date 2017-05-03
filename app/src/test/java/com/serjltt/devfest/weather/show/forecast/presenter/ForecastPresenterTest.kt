package com.serjltt.devfest.weather.show.forecast.presenter

import com.serjltt.devfest.weather.any
import com.serjltt.devfest.weather.assertCalled
import com.serjltt.devfest.weather.assertNeverCalled
import com.serjltt.devfest.weather.mockReturn
import com.serjltt.devfest.weather.mvp.Presenter
import com.serjltt.devfest.weather.show.forecast.ForecastMvp
import com.serjltt.devfest.weather.show.forecast.model.ForecastData
import com.serjltt.devfest.weather.show.forecast.model.ForecastModel
import com.serjltt.devfest.weather.show.forecast.usecase.GetForecastUseCase
import com.serjltt.devfest.weather.toJustObservable
import io.reactivex.subjects.BehaviorSubject
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Matchers.anyString
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

class ForecastPresenterTest {
  @Suppress("unused")
  @get:Rule val mockitoRule: MockitoRule = MockitoJUnit.rule()

  @Mock lateinit var useCase: GetForecastUseCase
  @Mock lateinit var view: ForecastMvp.View

  private val subject: BehaviorSubject<String> = BehaviorSubject.create<String>()

  private lateinit var presenter: Presenter<ForecastMvp.View>

  @Before @Throws(Exception::class)
  fun setUp() {
    view.cityName().mockReturn(subject)

    presenter = ForecastPresenter(useCase)
  }

  @Test fun propagatesError() {
    val error = ForecastModel.Error(Exception("Error"))
    useCase.getForecast(anyString()).mockReturn(error.toJustObservable())

    presenter.bind(view)
    triggerEvent("test")

    view.assertCalled(1).updateView(error)
  }

  @Test fun propagatesSuccess() {
    val oneForecast = ForecastData("date", "low", "high")
    val success = ForecastModel.Success(listOf(oneForecast))
    useCase.getForecast(anyString()).mockReturn(success.toJustObservable())

    presenter.bind(view)
    triggerEvent("test1")
    triggerEvent("test2")

    view.assertCalled(2).updateView(success)
  }

  @Test fun doesNotPropagateIfUnsubscribed() {
    useCase.getForecast(anyString())
        .mockReturn(ForecastModel.Success(emptyList()).toJustObservable())

    presenter.bind(view).dispose()
    triggerEvent("test1")

    view.assertNeverCalled().updateView(any())
  }

  private fun triggerEvent(value: String) {
    subject.onNext(value)
  }
}
