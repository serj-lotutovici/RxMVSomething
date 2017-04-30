package com.serjltt.devfest.weather.show.forecast.ui

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.f2prateek.rx.preferences2.Preference
import com.pedrogomez.renderers.ListAdapteeCollection
import com.pedrogomez.renderers.RVRendererAdapter
import com.pedrogomez.renderers.RendererBuilder
import com.serjltt.devfest.weather.R
import com.serjltt.devfest.weather.di.Injector
import com.serjltt.devfest.weather.di.InjectorActivity
import com.serjltt.devfest.weather.mvp.Presenter
import com.serjltt.devfest.weather.show.forecast.ForecastMvp
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class ForecastActivity : InjectorActivity(), ForecastMvp.View {
  @BindView(R.id.forecast_list) lateinit var recyclerView: RecyclerView
  @BindView(R.id.forecast_progress) lateinit var progressView: ProgressBar
  @BindView(R.id.forecast_error) lateinit var errorView: TextView
  @BindView(R.id.forecast_toolbar) lateinit var toolbarView: Toolbar

  @Inject lateinit var presenter: Presenter<ForecastMvp.View>
  @Inject lateinit var cityNamePref: Preference<String>

  lateinit var adapter: RVRendererAdapter<ForecastMvp.Model>
  lateinit var disposable: Disposable
  lateinit var unbinder: Unbinder

  override fun onInject(injector: Injector) {
    DaggerForecastUiComponent.builder().injector(injector).build().inject(this)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_forecast)
    unbinder = ButterKnife.bind(this)

    setSupportActionBar(toolbarView)
    setCity(cityNamePref.get())

    val rendererBuilder = RendererBuilder<ForecastMvp.Model>(ModelRenderer())
    adapter = RVRendererAdapter<ForecastMvp.Model>(rendererBuilder,
        ListAdapteeCollection<ForecastMvp.Model>())
    recyclerView.layoutManager = LinearLayoutManager(this)
    recyclerView.adapter = adapter

    disposable = presenter.bind(this)
  }

  override fun onDestroy() {
    disposable.dispose()
    unbinder.unbind()
    super.onDestroy()
  }

  override fun showLoading() {
    progressView.visibility = View.VISIBLE
    errorView.visibility = View.GONE
  }

  override fun hideLoading() {
    progressView.visibility = View.GONE
  }

  override fun showError(error: String?) {
    errorView.visibility = View.VISIBLE
    errorView.text = error
  }

  override fun showForecast(data: List<ForecastMvp.Model>) {
    adapter.clear()
    adapter.addAll(data)
    adapter.notifyDataSetChanged()
  }

  override fun cityName(): Observable<String> {
    return cityNamePref.asObservable().share()
  }

  @OnClick(R.id.forecast_fab) internal fun showChangeCityDialog() {
    AlertDialog.Builder(this).setItems(CITIES) { _, which ->
      val name = CITIES[which]
      setCity(name)
      cityNamePref.set(name)
    }.create().show()
  }

  internal fun setCity(name: String) {
    supportActionBar!!.title = getString(R.string.screen_name, name)
  }

  companion object {
    internal val CITIES = arrayOf("amsterdam", "hamburg", "barcelona")
  }
}
