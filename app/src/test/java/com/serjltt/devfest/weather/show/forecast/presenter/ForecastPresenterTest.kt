package com.serjltt.devfest.weather.show.forecast.presenter

import com.serjltt.devfest.weather.any
import com.serjltt.devfest.weather.assertCalled
import com.serjltt.devfest.weather.assertNeverCalled
import com.serjltt.devfest.weather.mockReturn
import com.serjltt.devfest.weather.mvp.Presenter
import com.serjltt.devfest.weather.rx.StubPreference
import com.serjltt.devfest.weather.rx.toObservable
import com.serjltt.devfest.weather.show.forecast.ForecastData
import com.serjltt.devfest.weather.show.forecast.ForecastUiModel
import com.serjltt.devfest.weather.show.forecast.ForecastView
import com.serjltt.devfest.weather.show.forecast.usecase.GetForecastResult
import com.serjltt.devfest.weather.show.forecast.usecase.GetForecastUseCase
import com.serjltt.devfest.weather.show.forecast.usecase.SelectCityResult
import com.serjltt.devfest.weather.show.forecast.usecase.SelectCityUseCase
import io.reactivex.rxkotlin.toSingle
import io.reactivex.subjects.BehaviorSubject
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Matchers.anyString
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

@Suppress("IllegalIdentifier")
class ForecastPresenterTest {
  @Suppress("unused")
  @get:Rule val mockitoRule: MockitoRule = MockitoJUnit.rule()

  @Mock lateinit var getForecastUseCase: GetForecastUseCase
  @Mock lateinit var selectCityUseCase: SelectCityUseCase
  @Mock lateinit var view: ForecastView

  private val clickSubject: BehaviorSubject<Unit> = BehaviorSubject.create()
  private val cityPreference = StubPreference<String>()

  private lateinit var presenter: Presenter<ForecastView>

  @Before fun setUp() {
    view.selectCity().mockReturn(clickSubject)

    presenter = ForecastPresenter(cityPreference, getForecastUseCase, selectCityUseCase)
  }

  @Test fun `Presenter propagates error model`() {
    val error = GetForecastResult.Error("Error")
    getForecastUseCase.getForecast(anyString()).mockReturn(error.toObservable())

    presenter.bind(view)
    cityPreference.set("test1")

    view.assertCalled(1).updateView(ForecastUiModel.Error("Error"))
  }

  @Test fun `Presenter propagates success model`() {
    val forecastData = listOf(ForecastData("date", "low", "high"))
    val success = GetForecastResult.Success(forecastData)

    val forecastSubject: BehaviorSubject<GetForecastResult> = BehaviorSubject.create()
    getForecastUseCase.getForecast(anyString()).mockReturn(forecastSubject)

    presenter.bind(view)

    cityPreference.set("test1")
    forecastSubject.onNext(GetForecastResult.Progress)
    forecastSubject.onNext(success)
    view.assertCalled(1).updateView(ForecastUiModel.Progress)
    view.assertCalled(1).updateView(ForecastUiModel.Success("test1", forecastData))

    cityPreference.set("test2")
    view.assertCalled(1).updateView(ForecastUiModel.Success("test2", forecastData))
  }

  @Test fun `Presenter reacts to click events`() {
    val testCityObservable = cityPreference.asObservable().test()

    presenter.bind(view)
    view.assertNeverCalled().updateView(any())

    selectCityUseCase.selectCity().mockReturn(SelectCityResult.Canceled.toSingle())
    clickSubject.onNext(Unit)
    testCityObservable.assertNoValues()

    getForecastUseCase.getForecast(anyString())
        .mockReturn(GetForecastResult.Progress.toObservable())
    selectCityUseCase.selectCity().mockReturn(SelectCityResult.NewCity("test").toSingle())
    clickSubject.onNext(Unit)
    testCityObservable.assertValue("test")
    view.assertCalled(1).updateView(ForecastUiModel.Progress)
  }

  @Test fun `Presenter does not propagate when disposed`() {
    getForecastUseCase.getForecast(anyString())
        .mockReturn(GetForecastResult.Success(emptyList()).toObservable())

    presenter.bind(view).dispose()
    cityPreference.set("test1")

    view.assertNeverCalled().updateView(any())
  }
}
