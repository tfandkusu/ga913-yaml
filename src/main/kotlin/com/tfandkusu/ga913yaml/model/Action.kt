package com.tfandkusu.ga913yaml.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 画面内操作イベント
 * @param description 説明文。ドキュメントとイベントクラスへのコメントに採用される。
 * @param className Swift / Kotlin 上でのクラス名
 * @param value Analytics イベント名
 * @param isConversionEvent Firebase A/B Testing のために、コンバージョンイベントにする
 * @param parameters パラメータ一覧
 */
@Serializable
data class Action(
    val description: String,
    @SerialName("class")
    val className: String,
    val value: String,
    @SerialName("conversion")
    val isConversionEvent: Boolean = false,
    val parameters: List<Parameter> = listOf(),
)

/**
 * 画面内操作イベントのパラメータ
 */
@Serializable
data class Parameter(
    val variable: String,
    val value: String,
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
