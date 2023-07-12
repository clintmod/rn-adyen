package com.reactnativeadyen.utils

import com.adyen.checkout.core.api.Environment

fun getEnvironment(environment: String): Environment {
    return when(environment) {
        "test" ->
            Environment.TEST
        "live" ->
            Environment.LIVE
        "liveEurope" ->
            Environment.EUROPE
        "liveAustralia" ->
            Environment.AUSTRALIA
        "liveUnitedStates" ->
            Environment.UNITED_STATES
        else ->
            Environment.TEST
    }
}
