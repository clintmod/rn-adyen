package com.reactnativeadyen.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.lifecycle.Observer
import com.adyen.checkout.components.GenericComponentState
import com.adyen.checkout.components.model.payments.request.EPSPaymentMethod
import com.adyen.checkout.eps.EPSComponent
import com.adyen.checkout.eps.EPSConfiguration
import com.adyen.checkout.eps.EPSRecyclerView
import com.google.gson.Gson
import com.reactnativeadyen.R
import com.reactnativeadyen.extensions.toWritableMap
import com.reactnativeadyen.utils.getEnvironment

class EpsComponentDialogActivity : ComponentDialogActivity() {
  private lateinit var epsComponent: EPSComponent
  private var observer: Observer<GenericComponentState<EPSPaymentMethod>> = Observer {
    if (it.isValid) {
      promise.resolve(Gson().toJson(it).toString().toWritableMap())
      dismiss()
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val epsConfiguration = EPSConfiguration.Builder(this, clientKey)
      .setEnvironment(getEnvironment(environment))
      .build()
    epsComponent = EPSComponent.PROVIDER.get(this, paymentMethod, epsConfiguration)
    epsComponent.observe(this, observer)

    show()
  }

  override fun show() {
    val epsView = init<EPSRecyclerView>(R.layout.eps_view_dialog, R.id.epsView)
    epsView?.attach(epsComponent, this)

    super.show()
  }
}
