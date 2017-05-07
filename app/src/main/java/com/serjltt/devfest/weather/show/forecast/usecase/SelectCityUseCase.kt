package com.serjltt.devfest.weather.show.forecast.usecase

import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import com.serjltt.devfest.weather.di.Consumer
import com.serjltt.devfest.weather.rx.RxModule
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.SingleEmitter
import javax.inject.Inject
import javax.inject.Named

interface SelectCityUseCase {
  fun selectCity(): Single<SelectCityResult>
}

@Consumer internal class SelectCityUseCaseImpl @Inject internal constructor(
    private val activity: AppCompatActivity,
    @param:Named(RxModule.MAIN_SCHEDULER) private val mainThreadScheduler: Scheduler)
  : SelectCityUseCase {

  override fun selectCity(): Single<SelectCityResult> =
      Single.create { emitter: SingleEmitter<SelectCityResult> ->
        val dialog = AlertDialog.Builder(activity)
            .setItems(CITIES) { _, which ->
              emitter.onSuccess(SelectCityResult.NewCity(CITIES[which]))
            }
            .setOnCancelListener { emitter.onSuccess(SelectCityResult.Canceled) }
            .create()

        emitter.setCancellable { if (dialog.isShowing) dialog.dismiss() }

        dialog.show()
      }
          .subscribeOn(mainThreadScheduler)
          .observeOn(mainThreadScheduler)

  private companion object {
    val CITIES = arrayOf("amsterdam", "hamburg", "barcelona")
  }
}

/** Result contract specific to [SelectCityUseCase]. */
sealed class SelectCityResult {
  data class NewCity(val name: String) : SelectCityResult()
  object Canceled : SelectCityResult()
}
