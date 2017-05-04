package com.serjltt.devfest.weather.show.forecast

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.serjltt.devfest.weather.di.Consumer
import com.serjltt.devfest.weather.mvp.Presenter
import com.serjltt.devfest.weather.show.forecast.presenter.ForecastPresenter
import com.serjltt.devfest.weather.show.forecast.view.ForecastActivity
import com.serjltt.devfest.weather.show.forecast.usecase.GetForecastUseCase
import com.serjltt.devfest.weather.show.forecast.usecase.GetForecastUseCaseImpl
import com.serjltt.devfest.weather.show.forecast.usecase.SelectCityUseCase
import com.serjltt.devfest.weather.show.forecast.usecase.SelectCityUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Consumer
@Subcomponent(modules = arrayOf(ForecastModule::class, ForecastBindModule::class))
interface ForecastUiComponent {
  fun inject(activity: ForecastActivity)
}

@Consumer @Module abstract class ForecastBindModule {
  @Binds internal abstract fun bindPresenter(presenter: ForecastPresenter)
      : Presenter<ForecastView>

  @Binds internal abstract fun bindGetForecastUseCase(useCase: GetForecastUseCaseImpl)
      : GetForecastUseCase

  @Binds internal abstract fun bindSelectCityUseCase(useCase: SelectCityUseCaseImpl)
      : SelectCityUseCase
}

@Consumer @Module class ForecastModule(private val activity: AppCompatActivity) {
  @Consumer @Provides fun provideActivity(): AppCompatActivity = activity

  @Consumer @Provides internal fun provideSharedPreferences(context: Context) =
      context.getSharedPreferences("forecast", Context.MODE_PRIVATE)

  @Consumer @Provides internal fun provideRxSharedPreferences(preferences: SharedPreferences) =
      RxSharedPreferences.create(preferences)

  @Consumer @Provides internal fun provideCityPreference(rxPreferences: RxSharedPreferences) =
      rxPreferences.getString("city", "amsterdam")
}
