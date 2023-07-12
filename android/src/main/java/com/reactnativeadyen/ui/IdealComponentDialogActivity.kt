package com.reactnativeadyen.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.lifecycle.Observer
import com.adyen.checkout.components.GenericComponentState
import com.adyen.checkout.components.model.payments.request.IdealPaymentMethod
import com.adyen.checkout.ideal.IdealComponent
import com.adyen.checkout.ideal.IdealConfiguration
import com.adyen.checkout.ideal.IdealRecyclerView
import com.google.gson.Gson
import com.reactnativeadyen.R
import com.reactnativeadyen.extensions.toWritableMap
import com.reactnativeadyen.utils.getEnvironment

class IdealComponentDialogActivity : ComponentDialogActivity() {
  private lateinit var idealComponent: IdealComponent
  private var observer: Observer<GenericComponentState<IdealPaymentMethod>> = Observer {
    if (it.isValid) {
      promise.resolve(Gson().toJson(it).toString().toWritableMap())
      dismiss()
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val idealConfiguration = IdealConfiguration.Builder(this, clientKey)
      .setEnvironment(getEnvironment(environment))
      .build()
    idealComponent = IdealComponent.PROVIDER.get(this, paymentMethod, idealConfiguration)
    idealComponent.observe(this, observer)

    show()
  }

  override fun show() {
    val idealView = init<IdealRecyclerView>(R.layout.ideal_view_dialog, R.id.idealView)
    idealView?.attach(idealComponent, this)

    super.show()
  }
}
