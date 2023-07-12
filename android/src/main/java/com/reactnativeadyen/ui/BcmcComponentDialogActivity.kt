package com.reactnativeadyen.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.adyen.checkout.bcmc.BcmcComponent
import com.adyen.checkout.bcmc.BcmcConfiguration
import com.adyen.checkout.bcmc.BcmcView
import com.adyen.checkout.components.GenericComponentState
import com.adyen.checkout.components.model.payments.request.CardPaymentMethod
import com.google.gson.Gson
import com.reactnativeadyen.R
import com.reactnativeadyen.extensions.toWritableMap
import com.reactnativeadyen.utils.getEnvironment

class BcmcComponentDialogActivity : ComponentDialogActivity() {

  private lateinit var bcmcComponent: BcmcComponent
  private var state: GenericComponentState<CardPaymentMethod>? = null
  private var observer: Observer<GenericComponentState<CardPaymentMethod>> = Observer {
    if (it.isValid) {
      state = it
    }
  }
  private var listener: View.OnClickListener = View.OnClickListener {
    if (state?.isValid == true) {
      promise.resolve(Gson().toJson(state).toString().toWritableMap())
      dismiss()
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val bcmcConfiguration = BcmcConfiguration.Builder(this, clientKey)
      .setEnvironment(getEnvironment(environment))
      .build()
    bcmcComponent = BcmcComponent.PROVIDER.get(this, paymentMethod, bcmcConfiguration)
    bcmcComponent.observe(this, observer)

    show()
  }

  override fun show() {
    val bcmcView = init<BcmcView>(R.layout.bancontact_view_dialog, R.id.bcmcView)
    findBottomSheetDialogViewById<Button>(R.id.pay_button)?.setOnClickListener(listener)
    bcmcView?.attach(bcmcComponent, this)

    super.show()
  }
}
