package com.tfandkusu.ga913yaml

import com.tfandkusu.ga913yaml.model.Screen
import java.io.File

object MarkdownGenerator {
    fun generate(screens: List<Screen>) {
        val builder = StringBuilder()
        builder.append("# 画面遷移イベント一覧\n\n")
        builder.append("| 画面名 | Analytics イベント名 | コンバージョンイベント |\n")
        builder.append("| -- | -- | -- |\n")
        for (screen in screens) {
            builder.append("| ${screen.description} | ${screen.eventName} | ${screen.isConversionEvent.toCircle()} |\n")
        }
        File("screens.md").writeText(builder.toString())
    }

    private fun Boolean.toCircle(): String =
        if (this) {
            "○"
        } else {
            ""
        }
}
