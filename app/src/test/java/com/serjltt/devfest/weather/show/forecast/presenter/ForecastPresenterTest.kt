package com.serjltt.devfest.weather.show.forecast.presenter

import com.serjltt.devfest.weather.assertCalled
import com.serjltt.devfest.weather.assertNeverCalled
import com.serjltt.devfest.weather.mockReturn
import com.serjltt.devfest.weather.mvp.Presenter
import com.serjltt.devfest.weather.show.forecast.ForecastMvp
import com.serjltt.devfest.weather.show.forecast.usecase.GetForecastUseCase
import com.serjltt.devfest.weather.toJustObservable
import io.reactivex.Observable
import io.reactivex.schedulers.TestScheduler
import io.reactivex.subjects.BehaviorSubject
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Matchers.anyListOf
import org.mockito.Matchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

class ForecastPresenterTest {
  @Suppress("unused")
  @get:Rule val mockitoRule: MockitoRule = MockitoJUnit.rule()

  @Mock lateinit var useCase: GetForecastUseCase
  @Mock lateinit var view: ForecastMvp.View

  private val testScheduler: TestScheduler = TestScheduler()
  private val subject: BehaviorSubject<String> = BehaviorSubject.create<String>()

  private lateinit var presenter: Presenter<ForecastMvp.View>

  @Before @Throws(Exception::class)
  fun setUp() {
    view.cityName().mockReturn(subject)

    presenter = ForecastPresenter(useCase, testScheduler, testScheduler)
  }

  @Test fun propagatesError() {
    useCase.getForecast(anyString()).mockReturn(Observable.error(Exception("Error")))

    presenter.bind(view)
    triggerEvent("test")

    view.assertCalled(1).showLoading()
    view.assertCalled(1).hideLoading()
    view.assertCalled(1).showError("Error")
  }

  @Test fun propagatesSuccess() {
    val oneForecast = mock(ForecastMvp.Model::class.java)
    useCase.getForecast(anyString())
        .mockReturn(listOf<ForecastMvp.Model>(oneForecast).toJustObservable())

    presenter.bind(view)
    triggerEvent("test1")
    triggerEvent("test2")

    view.assertCalled(2).showLoading()
    view.assertCalled(2).hideLoading()
    view.assertCalled(2).showForecast(listOf<ForecastMvp.Model>(oneForecast))
  }

  @Test fun doesNotPropagateIfUnsubscribed() {
    useCase.getForecast(anyString())
        .mockReturn(emptyList<ForecastMvp.Model>().toJustObservable())

    presenter.bind(view).dispose()
    triggerEvent("test1")

    view.assertNeverCalled().showLoading()
    view.assertNeverCalled().hideLoading()
    view.assertNeverCalled().showForecast(anyListOf(ForecastMvp.Model::class.java))
  }

  private fun triggerEvent(value: String) {
    subject.onNext(value)
    testScheduler.triggerActions()
  }
}
