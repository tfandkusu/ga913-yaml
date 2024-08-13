package com.tfandkusu.ga913yaml

import com.tfandkusu.ga913yaml.model.Screen
import io.outfoxx.swiftpoet.FileSpec
import io.outfoxx.swiftpoet.TypeSpec

object SwiftGenerator {
    private const val ROOT_STRUCT = "AnalyticsEvent"

    fun generate(screens: List<Screen>) {
        FileSpec
            .builder(ROOT_STRUCT)
            .addComment("https://github.com/tfandkusu/ga913-yaml/ による自動生成コードです。編集しないでください。")
            .addType(
                TypeSpec.structBuilder(ROOT_STRUCT).build(),
            ).build()
            .writeTo(System.out)
    }
}
