package com.tfandkusu.ga913yaml.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 画面内操作イベント
 * @param description 説明文。ドキュメントとイベントクラスへのコメントに採用される。
 * @param className Swift / Kotlin 上でのクラス名
 * @param eventName Analytics イベント名
 * @param isConversionEvent Firebase A/B Testing のために、コンバージョンイベントにする
 * @param parameters パラメータ一覧
 */
@Serializable
data class Action(
    val description: String,
    @SerialName("class")
    val className: String,
    @SerialName("value")
    val eventName: String,
    @SerialName("conversion")
    val isConversionEvent: Boolean = false,
    val parameters: List<Parameter> = listOf(),
)

/**
 * 画面内操作イベントのパラメータ
 *
 * @param description 説明文。ドキュメントとイベントクラスのフィールドへのコメントに採用される。
 * @param propertyName Swift / Kotlin 上でのプロパティ名
 * @param eventParameterKey Analytics イベントパラメータのキー
 * @param type パラメータの型
 */
@Serializable
data class Parameter(
    val description: String,
    @SerialName("property")
    val propertyName: String,
    @SerialName("key")
    val eventParameterKey: String,
    val type: ParameterType,
)

@Serializable
enum class ParameterType {
    @SerialName("string")
    STRING,

    @SerialName("int")
    INT,

    @SerialName("long")
    LONG,

    @SerialName("float")
    FLOAT,

    @SerialName("double")
    DOUBLE,

    @SerialName("boolean")
    BOOLEAN,
}
