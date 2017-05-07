package com.serjltt.devfest.weather.show.forecast.view

import android.support.test.rule.ActivityTestRule
import com.serjltt.devfest.weather.show.forecast.ForecastData
import com.serjltt.devfest.weather.show.forecast.ForecastUiModel
import com.serjltt.devfest.weather.show.forecast.ForecastView
import io.reactivex.android.schedulers.AndroidSchedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ForecastActivityTest {
  @get:Rule val activityTestRule = ActivityTestRule(ForecastActivity::class.java)

  private val screen = ForecastActivityScreen()
  private lateinit var view: ForecastView

  @Before fun setUp() {
    view = activityTestRule.activity
  }

  @Test fun displaysStateAppropriately() {
    runOnUiThread { view.updateView(ForecastUiModel.Progress) }
    screen.assertProgressVisible()

    val error = "HTTP 501 Server Error"
    runOnUiThread { view.updateView(ForecastUiModel.Error(error)) }
    screen.assertErrorVisible(error)

    val data = listOf(
        ForecastData("12 May 2017", "23", "34"),
        ForecastData("13 May 2017", "22", "42"),
        ForecastData("14 May 2017", "24", "43"))
    val success = ForecastUiModel.Success("Z", data)
    runOnUiThread { view.updateView(success) }
    screen.assertSuccessVisible(success)
  }

  @Test fun propagatesClick() {
    val testObserver = view.selectCity()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(AndroidSchedulers.mainThread())
        .test()

    screen.clickOnFab()

    testObserver
        .assertNoErrors()
        .assertValue(Unit)
  }

  private fun runOnUiThread(block: () -> Unit) = activityTestRule.runOnUiThread { block() }
}
