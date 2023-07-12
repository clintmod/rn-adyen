package com.reactnativeadyen.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.adyen.checkout.card.CardComponent
import com.adyen.checkout.card.CardComponentState
import com.adyen.checkout.card.CardConfiguration
import com.adyen.checkout.card.CardView
import com.adyen.checkout.card.CardListAdapter
import com.adyen.checkout.card.data.CardType
import com.adyen.checkout.components.api.ImageLoader
import com.google.gson.Gson
import com.reactnativeadyen.R
import com.reactnativeadyen.extensions.toWritableMap
import com.reactnativeadyen.utils.getEnvironment

class CardComponentDialogActivity : ComponentDialogActivity() {

  private lateinit var cardComponent: CardComponent
  private lateinit var cardListAdapter: CardListAdapter
  private var state: CardComponentState? = null
  private var observer: Observer<CardComponentState> = Observer {
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

    val cardConfiguration = CardConfiguration.Builder(this, clientKey)
      .setHolderNameRequired(false)
      .setEnvironment(getEnvironment(environment))
      .build()

    cardComponent = CardComponent.PROVIDER.get(this, paymentMethod, cardConfiguration)

    val supportedCards = if (cardComponent.isStoredPaymentMethod()) {
      emptyList<CardType>()
    } else {
      cardComponent.configuration.supportedCardTypes
    }
    cardListAdapter = CardListAdapter(
      ImageLoader.getInstance(this, cardComponent.configuration.environment),
      supportedCards
    )

    cardComponent.observe(this, observer)

    show()
  }

  override fun show() {
    val cardView = init<CardView>(R.layout.card_view_dialog, R.id.cardView)
    findBottomSheetDialogViewById<Button>(R.id.pay_button)?.setOnClickListener(listener)
    val recyclerViewCardList = findBottomSheetDialogViewById<RecyclerView>(R.id.recyclerView_cardList)
    recyclerViewCardList?.adapter = cardListAdapter
    cardView?.attach(cardComponent, this)

    super.show()
  }
}
