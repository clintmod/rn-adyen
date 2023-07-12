package com.reactnativeadyen.extensions

import com.facebook.react.bridge.ReadableMap
import com.google.gson.Gson
import org.json.JSONObject

fun <T> ReadableMap.decoded(classType: Class<T>): T? {
    val jsonString = JSONObject(this.toHashMap()).toString()

    return Gson().fromJson(jsonString, classType)
}

fun ReadableMap.toJsonString(): String {
    return JSONObject(this.toHashMap()).toString()
}