package com.reactnativeadyen

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.adyen.checkout.adyen3ds2.Adyen3DS2Component
import com.adyen.checkout.adyen3ds2.Adyen3DS2Configuration
import com.adyen.checkout.components.ActionComponentData
import com.adyen.checkout.components.ComponentError
import com.adyen.checkout.components.base.BaseActionComponent
import com.adyen.checkout.components.base.Configuration
import com.adyen.checkout.components.model.paymentmethods.PaymentMethod
import com.adyen.checkout.components.model.payments.response.Action
import com.adyen.checkout.components.model.payments.response.RedirectAction
import com.adyen.checkout.components.model.payments.response.Threeds2FingerprintAction
import com.adyen.checkout.components.model.payments.response.Threeds2ChallengeAction
import com.adyen.checkout.components.util.PaymentMethodTypes
import com.adyen.checkout.cse.CardEncrypter
import com.adyen.checkout.cse.UnencryptedCard
import com.adyen.checkout.cse.exception.EncryptionException
import com.adyen.checkout.redirect.RedirectComponent
import com.adyen.checkout.redirect.RedirectConfiguration
import com.adyen.checkout.redirect.RedirectUtil
import com.facebook.react.bridge.*
import com.facebook.react.util.RNLog
import com.reactnativeadyen.extensions.decoded
import com.reactnativeadyen.extensions.toMap
import com.reactnativeadyen.extensions.toWritableMap
import com.reactnativeadyen.models.RCTAction
import com.reactnativeadyen.models.RCTUnencryptedCard
import com.reactnativeadyen.ui.*
import com.reactnativeadyen.utils.getEnvironment
import java.util.*

class RCTAdyenModule(reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext), ActivityEventListener, LifecycleEventListener {

  override fun getName(): String {
    return "RCTAdyen"
  }

  init {
    reactContext.addActivityEventListener(this)
    reactContext.addLifecycleEventListener(this)
  }

  private var handler: Handler = Handler(Looper.getMainLooper())
  private var adyen3DS2Component: Adyen3DS2Component? = null
  private var redirectComponent: RedirectComponent? = null
  private lateinit var _promise: Promise
  private var actionType: String? = null
  private var actionResponse: String? = null

  private var actionObserver: Observer<ActionComponentData> = Observer {
    when (actionType) {
      "threeDS2Fingerprint" -> {
        if (it.details?.has("threeds2.fingerprint") == true) {
          _promise.resolve(it.details?.toString()?.toWritableMap())
        }
      }
      "threeDS2Challenge" -> {
        if (it.details?.has("threeds2.challengeResult") == true) {
          _promise.resolve(it.details?.toString()?.toWritableMap())
          cleanup()
        }
      }
      else -> {
        _promise.resolve(it.details.toString().toWritableMap())
      }
    }
    actionResponse = "success"
  }

  private var actionErrorObserver: Observer<ComponentError> = Observer {
    when (actionType) {
      "redirect" -> {
         _promise.reject(it.getException())
      }
      else -> {
        _promise.reject(it.getException())
        cleanup()
      }
    }
    actionResponse = "error"
  }

  @ReactMethod
  fun encrypt(publicKey: String, data: ReadableMap, promise: Promise) {
    try {
      val unencryptedCardData = data.decoded(RCTUnencryptedCard::class.java)
      val unencryptedCard = UnencryptedCard(
        unencryptedCardData?.number,
        unencryptedCardData?.expiryMonth,
        unencryptedCardData?.expiryYear,
        unencryptedCardData?.securityCode,
        unencryptedCardData?.holder,
        Date()
      )

      val encryptedCard = CardEncrypter.encryptFields(unencryptedCard, publicKey)

      promise.resolve(encryptedCard.toMap())
    } catch (error: EncryptionException) {
      promise.reject(error)
    }
  }

  @ReactMethod
  fun handleComponent(
    clientKey: String,
    environment: String,
    data: ReadableMap,
    amount: ReadableMap,
    promise: Promise
  ) {
    ComponentDialogActivity.promise = promise
    val paymentMethod = data.decoded(PaymentMethod::class.java)
    val intent = when (paymentMethod?.type) {
      PaymentMethodTypes.SCHEME -> Intent(reactApplicationContext, CardComponentDialogActivity::class.java)
      PaymentMethodTypes.BCMC -> Intent(reactApplicationContext, BcmcComponentDialogActivity::class.java)
      PaymentMethodTypes.IDEAL -> Intent(reactApplicationContext, IdealComponentDialogActivity::class.java)
      PaymentMethodTypes.EPS -> Intent(reactApplicationContext, EpsComponentDialogActivity::class.java)
      else -> null

    }
    intent?.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    intent?.putExtra("paymentMethod", paymentMethod)
    intent?.putExtra("clientKey", clientKey)
    intent?.putExtra("environment", environment)

    reactApplicationContext.startActivity(intent)
  }

  @ReactMethod
  fun handleAction(
    clientKey: String,
    environment: String,
    data: ReadableMap,
    promise: Promise
  ) {
    _promise = promise
    val actionData = data.decoded(RCTAction::class.java)
    actionType = actionData?.type

    when (actionData?.type) {
      "redirect" -> {
        val redirectAction = data.decoded(RedirectAction::class.java)
        val configuration =
          RedirectConfiguration.Builder(reactApplicationContext, clientKey)
            .setEnvironment(getEnvironment(environment))
            .build()

        handler.post {
          redirectComponent = RedirectComponent.PROVIDER.get(
            currentActivity as FragmentActivity,
            currentActivity?.application as Application,
            configuration
          )

          handleComponentAction(
            redirectComponent as BaseActionComponent<Configuration>,
            redirectAction as Action
          )
        }
      }
      "threeDS2Fingerprint" -> {
        val fingerprintAction = data.decoded(Threeds2FingerprintAction::class.java)
        val configuration =
          Adyen3DS2Configuration.Builder(reactApplicationContext, clientKey)
            .setEnvironment(getEnvironment(environment))
            .build()

        handler.post {
          adyen3DS2Component = Adyen3DS2Component.PROVIDER.get(
            currentActivity as FragmentActivity,
            currentActivity?.application as Application,
            configuration
          )

          handleComponentAction(
            adyen3DS2Component as BaseActionComponent<Configuration>,
            fingerprintAction as Action
          )
        }
      }
      "threeDS2Challenge" -> {
        val challengeAction = data.decoded(Threeds2ChallengeAction::class.java)
        val configuration =
          Adyen3DS2Configuration.Builder(reactApplicationContext, clientKey)
            .setEnvironment(getEnvironment(environment))
            .build()
        handler.post {
          adyen3DS2Component = Adyen3DS2Component.PROVIDER.get(
            currentActivity as FragmentActivity,
            currentActivity?.application as Application,
            configuration
          )

          handleComponentAction(
            adyen3DS2Component as BaseActionComponent<Configuration>,
            challengeAction as Action
          )
        }
      }
    }
  }

  private fun handleComponentAction(
    actionComponent: BaseActionComponent<Configuration>,
    action: Action
  ) {
    actionComponent.handleAction(currentActivity as FragmentActivity, action)
    actionComponent.observe(currentActivity as FragmentActivity, actionObserver) 
    actionComponent.observeErrors(currentActivity as FragmentActivity, actionErrorObserver)    
  }

  override fun onActivityResult(
    activity: Activity?,
    requestCode: Int,
    resultCode: Int,
    data: Intent?
  ) {
  }

  override fun onNewIntent(intent: Intent?) {
    val data = intent?.data
    if (data != null && data.toString().startsWith(RedirectUtil.REDIRECT_RESULT_SCHEME)) {
      redirectComponent?.let {
        it.handleIntent(intent)
        redirectComponent = null
      }

      adyen3DS2Component?.let {
        it.handleIntent(intent)
        adyen3DS2Component = null
      }
    }
  }

  fun cleanup() {
    redirectComponent?.let {
      it.removeObserver(actionObserver)
      it.removeErrorObserver(actionErrorObserver)
      redirectComponent = null
    }

    adyen3DS2Component?.let {
      it.removeObserver(actionObserver)
      it.removeErrorObserver(actionErrorObserver)
      adyen3DS2Component = null
    }
  }

  override fun onHostResume() {
    if (actionResponse == null && actionType != null) {
      _promise.reject("1", "Transaction Cancelled")
    }
    actionType = null
    actionResponse = null
  }

  override fun onHostPause() {}
  override fun onHostDestroy() {}
}
