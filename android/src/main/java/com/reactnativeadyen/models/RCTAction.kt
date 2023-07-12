package com.reactnativeadyen.models

data class RCTAction (
    val paymentMethodType: String? = null,
    val url: String? = null,
    val method: String? = null,
    val type: String? = null,
    val paymentData: String? = null,
    val authorisationToken: String? = null,
    val subtype: String? = null,
    val token: String? = null
)