package com.reactnativeadyen.models

data class RCTUnencryptedCard (
    val number: String? = null,
    val securityCode: String? = null,
    val expiryMonth: String? = null,
    val expiryYear: String? = null,
    val holder: String? = null
)