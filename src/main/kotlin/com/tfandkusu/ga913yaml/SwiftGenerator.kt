package com.tfandkusu.ga913yaml

import com.tfandkusu.ga913yaml.model.Screen
import io.outfoxx.swiftpoet.ANY
import io.outfoxx.swiftpoet.BOOL
import io.outfoxx.swiftpoet.DICTIONARY
import io.outfoxx.swiftpoet.DeclaredTypeName
import io.outfoxx.swiftpoet.FileSpec
import io.outfoxx.swiftpoet.PropertySpec
import io.outfoxx.swiftpoet.STRING
import io.outfoxx.swiftpoet.TypeSpec
import io.outfoxx.swiftpoet.parameterizedBy

object SwiftGenerator {
    private const val ROOT_STRUCT = "AnalyticsEvent"
    private val SCREEN_PROTOCOL = DeclaredTypeName.typeName(".AnalyticsEventScreen")
    private val ACTION_PROTOCOL = DeclaredTypeName.typeName(".AnalyticsEventAction")

    fun generate(screens: List<Screen>) {
        FileSpec
            .builder(ROOT_STRUCT)
            .addComment("https://github.com/tfandkusu/ga913-yaml/ による自動生成コードです。編集しないでください。")
            .addType(
                generateAnalyticsEventScreenProtocol(),
            ).addType(
                generateAnalyticsEventActionProtocol(),
            ).addType(
                TypeSpec
                    .structBuilder(ROOT_STRUCT)
                    .addDoc("Analytics イベント構造体群")
                    .addType(
                        generateScreenStruct(screens),
                    ).addType(
                        generateActionStruct(screens),
                    ).build(),
            ).build()
            .writeTo(System.out)
    }

    private fun generateAnalyticsEventScreenProtocol(): TypeSpec =
        TypeSpec
            .protocolBuilder(SCREEN_PROTOCOL)
            .addDoc("画面遷移イベントのプロトコル")
            .addProperty(
                PropertySpec
                    .abstractBuilder("screenName", STRING)
                    .abstractGetter()
                    .addDoc("Analytics イベント名")
                    .build(),
            ).addProperty(
                PropertySpec
                    .abstractBuilder("isConversionEvent", BOOL)
                    .abstractGetter()
                    .addDoc("コンバージョンイベントフラグ")
                    .build(),
            ).build()

    private fun generateAnalyticsEventActionProtocol(): TypeSpec =
        TypeSpec
            .protocolBuilder(ACTION_PROTOCOL)
            .addDoc("画面内操作イベントのプロトコル")
            .addProperty(
                PropertySpec
                    .abstractBuilder("screenName", STRING)
                    .abstractGetter()
                    .addDoc("Analytics イベント名")
                    .build(),
            ).addProperty(
                PropertySpec
                    .abstractBuilder("eventParameters", DICTIONARY.parameterizedBy(STRING, ANY))
                    .abstractGetter()
                    .addDoc("Analytics イベントパラメータ")
                    .build(),
            ).addProperty(
                PropertySpec
                    .abstractBuilder("isConversionEvent", BOOL)
                    .abstractGetter()
                    .addDoc("コンバージョンイベントフラグ")
                    .build(),
            ).build()

    private fun generateScreenStruct(screens: List<Screen>): TypeSpec =
        TypeSpec
            .structBuilder("Screen")
            .apply {
                screens.forEach { screen ->
                    addType(
                        generateScreenStruct(screen),
                    )
                }
            }.build()

    private fun generateScreenStruct(screen: Screen): TypeSpec =
        TypeSpec
            .structBuilder(screen.className)
            .addSuperType(SCREEN_PROTOCOL)
            .addProperty(
                PropertySpec
                    .builder("screenName", STRING)
                    .initializer("%S", screen.eventName)
                    .build(),
            ).addProperty(
                PropertySpec
                    .builder("isConversionEvent", BOOL)
                    .initializer("%L", screen.isConversionEvent)
                    .build(),
            ).build()

    private fun generateActionStruct(screens: List<Screen>): TypeSpec =
        TypeSpec
            .structBuilder("Action")
            .apply {
                screens.forEach { screen ->
                    addType(
                        generateActionScreenStruct(screen),
                    )
                }
            }.build()

    private fun generateActionScreenStruct(screen: Screen): TypeSpec = TypeSpec.structBuilder(screen.className).build()
}
