package com.reactnativeadyen.extensions

import com.facebook.react.bridge.*
import com.reactnativeadyen.utils.convertJsonToMap
import org.json.JSONObject

fun String.toWritableMap(): WritableMap? {
    val jsonObject = JSONObject(this)

    return convertJsonToMap(jsonObject)
}
