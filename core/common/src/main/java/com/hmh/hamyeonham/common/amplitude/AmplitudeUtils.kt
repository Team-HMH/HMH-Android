package com.hmh.hamyeonham.common.amplitude

import com.amplitude.api.Amplitude
import org.json.JSONObject

object AmplitudeUtils {
    private val amplitude by lazy { Amplitude.getInstance() }

    fun trackEventWithProperties(
        eventName: String,
        properties: JSONObject? = null,
    ) {
        if (properties == null) {
            amplitude.logEvent(eventName)
        } else {
            amplitude.logEvent(eventName, properties)
        }
    }
}
