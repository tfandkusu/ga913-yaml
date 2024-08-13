package com.tfandkusu.ga913yaml

import com.squareup.kotlinpoet.ANY
import com.squareup.kotlinpoet.BOOLEAN
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.DOUBLE
import com.squareup.kotlinpoet.FLOAT
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.INT
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LONG
import com.squareup.kotlinpoet.MAP
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.withIndent
import com.tfandkusu.ga913yaml.model.Action
import com.tfandkusu.ga913yaml.model.ParameterType
import com.tfandkusu.ga913yaml.model.Screen
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

object KotlinGenerator {
    private const val PACKAGE = "com.tfandkusu.ga913android.analytics"
    private const val DIRECTORY =
        "ga913-android/app/src/main/java/com/tfandkusu/ga913android/analytics"
    private const val ROOT_CLASS = "AnalyticsEvent"
    private const val SCREEN_CLASS = "Screen"
    private const val ACTION_CLASS = "Action"
    private const val EVENT_NAME_PROPERTY = "eventName"
    private const val EVENT_PARAMETERS_PROPERTY = "eventParameters"
    private const val IS_CONVERSION_EVENT_PROPERTY = "isConversionEvent"

    fun generate(screens: List<Screen>) {
        val fileSpec =
            FileSpec
                .builder(PACKAGE, ROOT_CLASS)
                .addFileComment(
                    "https://github.com/tfandkusu/ga913-yaml/ による自動生成コードです。編集しないでください。",
                ).addType(
                    generateAnalyticsEventClass(screens),
                ).build()
        Files.createDirectories(Paths.get(DIRECTORY))
        File("$DIRECTORY/$ROOT_CLASS.kt").writeText(fileSpec.toString())
    }

    private fun generateAnalyticsEventClass(screens: List<Screen>): TypeSpec =
        TypeSpec
            .objectBuilder(ROOT_CLASS)
            .addKdoc("Analytics イベントクラス群")
            .addType(
                generateScreenSealedClass(screens),
            ).addType(
                generateActionSealedClass(screens),
            ).build()

    private fun generateScreenSealedClass(screens: List<Screen>): TypeSpec =
        TypeSpec
            .classBuilder(SCREEN_CLASS)
            .addKdoc("画面遷移イベントクラス群")
            .addModifiers(KModifier.SEALED)
            .primaryConstructor(
                FunSpec
                    .constructorBuilder()
                    .addParameter(EVENT_NAME_PROPERTY, STRING)
                    .addParameter(IS_CONVERSION_EVENT_PROPERTY, BOOLEAN)
                    .build(),
            ).addProperty(
                PropertySpec
                    .builder(
                        EVENT_NAME_PROPERTY,
                        STRING,
                    ).initializer(EVENT_NAME_PROPERTY)
                    .addKdoc("Analytics イベント名")
                    .build(),
            ).addProperty(
                PropertySpec
                    .builder(
                        IS_CONVERSION_EVENT_PROPERTY,
                        BOOLEAN,
                    ).initializer(IS_CONVERSION_EVENT_PROPERTY)
                    .addKdoc("コンバージョンイベントフラグ")
                    .build(),
            ).apply {
                screens.forEach { screen ->
                    addType(generateScreenClass(screen))
                }
            }.build()

    private fun generateScreenClass(screen: Screen): TypeSpec =
        TypeSpec
            .objectBuilder(screen.className)
            .addKdoc(screen.description)
            .addModifiers(KModifier.DATA)
            .superclass(
                ClassName(
                    PACKAGE,
                    ROOT_CLASS,
                ).nestedClass(SCREEN_CLASS),
            ).addSuperclassConstructorParameter(
                "%S",
                screen.eventName,
            ).addSuperclassConstructorParameter(
                "%L",
                screen.isConversionEvent,
            ).build()

    private fun generateActionSealedClass(screens: List<Screen>): TypeSpec =
        TypeSpec
            .classBuilder(ACTION_CLASS)
            .addKdoc("画面内操作イベントクラス群")
            .addModifiers(KModifier.SEALED)
            .primaryConstructor(
                FunSpec
                    .constructorBuilder()
                    .addParameter(EVENT_NAME_PROPERTY, STRING)
                    .addParameter(
                        EVENT_PARAMETERS_PROPERTY,
                        MAP.parameterizedBy(STRING, ANY),
                    ).addParameter(IS_CONVERSION_EVENT_PROPERTY, BOOLEAN)
                    .build(),
            ).addProperty(
                PropertySpec
                    .builder(
                        EVENT_NAME_PROPERTY,
                        STRING,
                    ).initializer(EVENT_NAME_PROPERTY)
                    .addKdoc("Analytics イベント名")
                    .build(),
            ).addProperty(
                PropertySpec
                    .builder(
                        EVENT_PARAMETERS_PROPERTY,
                        MAP.parameterizedBy(STRING, ANY),
                    ).initializer(EVENT_PARAMETERS_PROPERTY)
                    .addKdoc("Analytics イベントパラメータ")
                    .build(),
            ).addProperty(
                PropertySpec
                    .builder(
                        IS_CONVERSION_EVENT_PROPERTY,
                        BOOLEAN,
                    ).initializer(IS_CONVERSION_EVENT_PROPERTY)
                    .addKdoc("コンバージョンイベントフラグ")
                    .build(),
            ).apply {
                screens.forEach { action ->
                    addType(generateActionScreenClass(action))
                }
            }.build()

    private fun generateActionScreenClass(screen: Screen): TypeSpec =
        TypeSpec
            .objectBuilder(screen.className)
            .addKdoc(screen.description)
            .apply {
                screen.actions.forEach { action ->
                    addType(
                        generateActionClass(
                            screenEventName = screen.eventName,
                            action = action,
                        ),
                    )
                }
            }.build()

    private fun generateActionClass(
        screenEventName: String,
        action: Action,
    ): TypeSpec {
        val builder =
            if (action.parameters.isEmpty()) {
                TypeSpec
                    .objectBuilder(action.className)
                    .addSuperclassConstructorParameter(
                        "%S",
                        screenEventName + action.eventName,
                    ).addSuperclassConstructorParameter("emptyMap()")
                    .addSuperclassConstructorParameter(
                        "%L",
                        action.isConversionEvent,
                    )
            } else {
                TypeSpec
                    .classBuilder(action.className)
                    .addModifiers(KModifier.DATA)
                    .primaryConstructor(
                        FunSpec
                            .constructorBuilder()
                            .apply {
                                action.parameters.forEach { parameter ->
                                    addParameter(
                                        parameter.propertyName,
                                        toKClass(parameter.type),
                                    )
                                }
                            }.build(),
                    ).addSuperclassConstructorParameter(
                        "%S",
                        screenEventName + action.eventName,
                    ).addSuperclassConstructorParameter(
                        CodeBlock
                            .builder()
                            .addStatement("mapOf(")
                            .withIndent {
                                withIndent {
                                    action.parameters.forEach { parameter ->
                                        addStatement(
                                            "%S to %L,",
                                            parameter.eventParameterKey,
                                            parameter.propertyName,
                                        )
                                    }
                                }
                                addStatement(")")
                            }.build(),
                    ).addSuperclassConstructorParameter(
                        "%L",
                        action.isConversionEvent,
                    ).apply {
                        action.parameters.forEach { parameter ->
                            addProperty(
                                PropertySpec
                                    .builder(
                                        parameter.propertyName,
                                        toKClass(parameter.type),
                                    ).initializer(parameter.propertyName)
                                    .addKdoc(parameter.description)
                                    .build(),
                            )
                        }
                    }
            }
        return builder
            .addKdoc(action.description)
            .superclass(
                ClassName(
                    PACKAGE,
                    ROOT_CLASS,
                ).nestedClass(ACTION_CLASS),
            ).build()
    }

    private fun toKClass(parameterType: ParameterType): ClassName =
        when (parameterType) {
            ParameterType.STRING -> STRING
            ParameterType.INT -> INT
            ParameterType.LONG -> LONG
            ParameterType.FLOAT -> FLOAT
            ParameterType.DOUBLE -> DOUBLE
            ParameterType.BOOLEAN -> BOOLEAN
        }
}
