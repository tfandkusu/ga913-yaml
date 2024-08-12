package com.tfandkusu.ga913yaml

import com.tfandkusu.ga913yaml.model.Screen

object Validator {
    fun validate(screens: List<Screen>) {
        checkEventNameLength(screens)
        checkEventCount(screens)
    }

    /**
     * イベント名の長さが40文字を超えていないかチェックする。
     */
    private fun checkEventNameLength(screens: List<Screen>) {
        for (screen in screens) {
            if (screen.eventName.length > 40) {
                throw IllegalArgumentException("画面遷移イベント " + screen.className + " は40文字を超えています。")
            }
            for (action in screen.actions) {
                if (screen.eventName.length + action.eventName.length > 40) {
                    throw IllegalArgumentException(
                        "画面内操作イベント " + screen.className + "." + action.className + " は40文字を超えています。",
                    )
                }
            }
        }
    }

    /**
     * イベント種類数が500を超えていないかチェックする。
     */
    private fun checkEventCount(screens: List<Screen>) {
        val eventCount =
            screens.count { it.isConversionEvent } +
                screens.flatMap { it.actions }.count { it.isConversionEvent }
        if (eventCount > 500) {
            throw IllegalArgumentException("イベント種類数が500を超えています。")
        }
    }
}
