package com.tfandkusu.ga913yaml

import com.tfandkusu.ga913yaml.model.Parameter
import com.tfandkusu.ga913yaml.model.ParameterType
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
        builder.append("# 画面内操作イベント一覧\n\n")
        for (screen in screens) {
            if (screen.actions.isNotEmpty()) {
                builder.append("## ${screen.description}\n\n")
                builder.append(generateActionsMarkdown(screen))
                builder.append("\n")
            }
        }
        File("actions.md").writeText(builder.toString())
    }

    private fun generateActionsMarkdown(screen: Screen): String {
        val builder = StringBuilder()
        builder.append("| 操作内容 | Analytics イベント名 | パラメータ | コンバージョンイベント |\n")
        builder.append("| -- | -- | -- | -- |\n")
        for (action in screen.actions) {
            builder.append(
                "| ${action.description} | ${screen.eventName + action.eventName} | ${
                    generateParameterListString(
                        action.parameters,
                    )
                } | ${action.isConversionEvent.toCircle()} |\n",
            )
        }
        return builder.toString()
    }

    private fun generateParameterListString(parameters: List<Parameter>): String {
        val builder = StringBuilder()
        for (parameter in parameters) {
            builder.append("${parameter.description} ${parameter.eventParameterKey}: ${parameter.type.toCamelCase()}<br>")
        }
        return builder.toString()
    }

    private fun Boolean.toCircle(): String =
        if (this) {
            "○"
        } else {
            ""
        }

    private fun ParameterType.toCamelCase(): String =
        when (this) {
            ParameterType.STRING -> "String"
            ParameterType.INT -> "Int"
            ParameterType.LONG -> "Int" // BigQuery には Long はない
            ParameterType.FLOAT -> "Float"
            ParameterType.DOUBLE -> "Double"
            ParameterType.BOOLEAN -> "Int" // BigQuery には Boolean はない
        }
}
