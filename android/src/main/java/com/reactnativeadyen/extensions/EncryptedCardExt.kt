package com.reactnativeadyen.extensions

import com.adyen.checkout.cse.EncryptedCard
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.WritableMap
import com.google.gson.JsonObject

fun EncryptedCard.toMap(): WritableMap {
    val map = Arguments.createMap()

    this.encryptedCardNumber?.let {
        map.putString("encryptedNumber", it)
    }

    this.encryptedExpiryMonth?.let {
        map.putString("encryptedExpiryMonth", it)
    }

    this.encryptedExpiryYear?.let {
        map.putString("encryptedExpiryYear", it)
    }

    this.encryptedSecurityCode?.let {
        map.putString("encryptedSecurityCode", it)
    }

    return map
}
