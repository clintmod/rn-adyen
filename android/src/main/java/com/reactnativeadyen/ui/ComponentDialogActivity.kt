package com.reactnativeadyen.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.adyen.checkout.components.model.paymentmethods.PaymentMethod
import com.facebook.react.bridge.Promise
import com.google.android.material.bottomsheet.BottomSheetDialog

open class ComponentDialogActivity : AppCompatActivity() {
  lateinit var bottomSheetDialog: BottomSheetDialog

  companion object {
    lateinit var paymentMethod: PaymentMethod
    lateinit var clientKey: String
    lateinit var environment: String
    lateinit var promise: Promise
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    paymentMethod = intent.extras!!.get("paymentMethod") as PaymentMethod
    clientKey = intent.extras!!.get("clientKey") as String
    environment = intent.extras!!.get("environment") as String
  }

  fun <T : View> init(layoutResId: Int, componentId: Int): T? {
    bottomSheetDialog = BottomSheetDialog(this)
    bottomSheetDialog.setContentView(layoutResId)

    return bottomSheetDialog.findViewById<T>(componentId)
  }

  fun <T: View> findBottomSheetDialogViewById(componentId: Int): T? {
    return bottomSheetDialog.findViewById<T>(componentId)
  }

  open fun show() {
    bottomSheetDialog.show()
    bottomSheetDialog.setOnDismissListener {
      finish()
    }
  }

  fun dismiss() {
    bottomSheetDialog.dismiss()
  }
}
