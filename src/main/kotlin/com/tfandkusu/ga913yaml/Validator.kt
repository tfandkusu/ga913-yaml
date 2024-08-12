package com.tfandkusu.ga913yaml

import com.tfandkusu.ga913yaml.model.Screen

object Validator {
    fun validate(screens: List<Screen>) {
        checkEventNameLength(screens)
    }

    private fun checkEventNameLength(screens: List<Screen>) {
        var eventCount = 0
        for (screen in screens) {
            if (screen.eventName.length > 40) {
                throw IllegalArgumentException("画面遷移イベント " + screen.className + " は40文字を超えています。")
            }
            if (screen.isConversionEvent) {
                eventCount++
            }
            for (action in screen.actions) {
                if (screen.eventName.length + action.eventName.length > 40) {
                    throw IllegalArgumentException(
                        "画面内操作イベント " + screen.className + "." + action.className + " は40文字を超えています。",
                    )
                }
                if (action.isConversionEvent) {
                    eventCount++
                }
            }
        }
        if (eventCount > 500) {
            throw IllegalArgumentException("イベント種類数が500を超えています。")
        }
    }
}
