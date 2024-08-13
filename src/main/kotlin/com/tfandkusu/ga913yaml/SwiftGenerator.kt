package com.tfandkusu.ga913yaml

import com.tfandkusu.ga913yaml.model.Action
import com.tfandkusu.ga913yaml.model.ParameterType
import com.tfandkusu.ga913yaml.model.Screen
import io.outfoxx.swiftpoet.ANY
import io.outfoxx.swiftpoet.BOOL
import io.outfoxx.swiftpoet.DICTIONARY
import io.outfoxx.swiftpoet.DOUBLE
import io.outfoxx.swiftpoet.DeclaredTypeName
import io.outfoxx.swiftpoet.FLOAT
import io.outfoxx.swiftpoet.FileSpec
import io.outfoxx.swiftpoet.FunctionSpec
import io.outfoxx.swiftpoet.INT
import io.outfoxx.swiftpoet.INT64
import io.outfoxx.swiftpoet.PropertySpec
import io.outfoxx.swiftpoet.STRING
import io.outfoxx.swiftpoet.TypeSpec
import io.outfoxx.swiftpoet.parameterizedBy

object SwiftGenerator {
    private const val ROOT_STRUCT = "AnalyticsEvent"
    private val SCREEN_PROTOCOL = DeclaredTypeName.typeName(".AnalyticsEventScreen")
    private val ACTION_PROTOCOL = DeclaredTypeName.typeName(".AnalyticsEventAction")
    private const val EVENT_NAME_PROPERTY = "eventName"
    private const val EVENT_PARAMETERS_PROPERTY = "eventParameters"
    private const val IS_CONVERSION_EVENT_PROPERTY = "isConversionEvent"

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
                    .abstractBuilder(EVENT_NAME_PROPERTY, STRING)
                    .abstractGetter()
                    .addDoc("Analytics イベント名")
                    .build(),
            ).addProperty(
                PropertySpec
                    .abstractBuilder(IS_CONVERSION_EVENT_PROPERTY, BOOL)
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
                    .abstractBuilder(EVENT_NAME_PROPERTY, STRING)
                    .abstractGetter()
                    .addDoc("Analytics イベント名")
                    .build(),
            ).addProperty(
                PropertySpec
                    .abstractBuilder(EVENT_PARAMETERS_PROPERTY, DICTIONARY.parameterizedBy(STRING, ANY))
                    .abstractGetter()
                    .addDoc("Analytics イベントパラメータ")
                    .build(),
            ).addProperty(
                PropertySpec
                    .abstractBuilder(IS_CONVERSION_EVENT_PROPERTY, BOOL)
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
                    .builder(EVENT_NAME_PROPERTY, STRING)
                    .initializer("%S", screen.eventName)
                    .build(),
            ).addProperty(
                PropertySpec
                    .builder(IS_CONVERSION_EVENT_PROPERTY, BOOL)
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

    private fun generateActionScreenStruct(screen: Screen): TypeSpec =
        TypeSpec
            .structBuilder(screen.className)
            .apply {
                screen.actions.forEach { action ->
                    addType(generateActionStruct(screen.eventName, action))
                }
            }.build()

    private fun generateActionStruct(
        screenName: String,
        action: Action,
    ): TypeSpec =
        TypeSpec
            .structBuilder(action.className)
            .addSuperType(ACTION_PROTOCOL)
            .addFunction(
                FunctionSpec
                    .constructorBuilder()
                    .apply {
                        action.parameters.forEach { parameter ->
                            addParameter(
                                parameter.propertyName,
                                toDeclaredTypeNames(parameter.type),
                            )
                        }
                    }.addStatement("parameters = [")
                    .apply {
                        action.parameters.forEach { parameter ->
                            addStatement(
                                "    %S: %L,",
                                parameter.eventParameterKey,
                                parameter.propertyName,
                            )
                        }
                    }.addStatement("]")
                    .build(),
            ).addProperty(
                PropertySpec
                    .builder(EVENT_NAME_PROPERTY, STRING)
                    .initializer("%S", screenName)
                    .build(),
            ).addProperty(
                PropertySpec
                    .builder(EVENT_PARAMETERS_PROPERTY, DICTIONARY.parameterizedBy(STRING, ANY))
                    .build(),
            ).addProperty(
                PropertySpec
                    .builder(IS_CONVERSION_EVENT_PROPERTY, BOOL)
                    .initializer("%L", action.isConversionEvent)
                    .build(),
            ).build()

    private fun toDeclaredTypeNames(parameterType: ParameterType): DeclaredTypeName =
        when (parameterType) {
            ParameterType.STRING -> STRING
            ParameterType.INT -> INT
            ParameterType.LONG -> INT64
            ParameterType.FLOAT -> FLOAT
            ParameterType.DOUBLE -> DOUBLE
            ParameterType.BOOLEAN -> BOOL
        }
}
