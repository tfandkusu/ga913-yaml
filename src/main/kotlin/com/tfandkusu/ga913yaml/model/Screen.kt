package com.tfandkusu.ga913yaml.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 画面遷移イベント
 * @param description 説明文。ドキュメントとイベントクラスへのコメントに採用される。
 * @param className Swift / Kotlin 上でのクラス名
 * @param value Analytics イベント名
 * @param isConversionEvent Firebase A/B Testing のために、コンバージョンイベントにする
 * @param actions 画面内の操作
 */
@Serializable
data class Screen(
    val description: String,
    @SerialName("class")
    val className: String,
    val value: String,
    @SerialName("conversion")
    val isConversionEvent: Boolean = false,
    val actions: List<Action> = listOf(),
)
