package com.tfandkusu.ga913yaml

import com.tfandkusu.ga913yaml.model.Screen
import java.io.File

object MarkdownGenerator {
    fun generate(screens: List<Screen>) {
        generateScreensMarkdown(screens)
        generateActionsMarkdown(screens)
    }

    private fun generateScreensMarkdown(screens: List<Screen>) {
        val builder = StringBuilder()
        builder.append("# 画面遷移イベント一覧\n\n")
        builder.append("| 画面名 | Analytics イベント名 | コンバージョンイベント |\n")
        builder.append("| -- | -- | -- |\n")
        for (screen in screens) {
            builder.append("| ${screen.description} | ${screen.eventName} | ${screen.isConversionEvent.toCircle()} |\n")
        }
        File("screens.md").writeText(builder.toString())
    }

    private fun generateActionsMarkdown(screens: List<Screen>) {
        val builder = StringBuilder()
        builder.append("# 画面内イベント一覧\n\n")
        for (screen in screens) {
            if (screen.actions.isNotEmpty()) {
                builder.append("# ${screen.description}\n\n")
                builder.append(generateActionsMarkdown(screen))
                builder.append("\n")
            }
        }
        File("actions.md").writeText(builder.toString())
    }

    private fun generateActionsMarkdown(screen: Screen): String {
        val builder = StringBuilder()
        builder.append("| イベント名 | Analytics イベント名 | コンバージョンイベント |\n")
        builder.append("| -- | -- | -- |\n")
        for (action in screen.actions) {
            builder.append("| ${action.description} | ${screen.eventName + action.eventName} | ${action.isConversionEvent.toCircle()} |\n")
        }
        return builder.toString()
    }

    private fun Boolean.toCircle(): String =
        if (this) {
            "○"
        } else {
            ""
        }
}
