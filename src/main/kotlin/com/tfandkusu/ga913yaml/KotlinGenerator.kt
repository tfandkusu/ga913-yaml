package com.tfandkusu.ga913yaml

import com.squareup.kotlinpoet.FileSpec
import com.tfandkusu.ga913yaml.model.Screen

object KotlinGenerator {
    const val PACKAGE = "com.tfandkusu.ga913android.analytics"
    const val DIRECTORY = "ga913-android/app/src/main/java/com/tfandkusu/ga913android/analytics"

    fun generate(screens: List<Screen>) {
        val fileSpec =
            FileSpec
                .builder(PACKAGE, "AnalyticsEvent")
                .addFileComment(
                    "https://github.com/tfandkusu/ga913-yaml/ による自動生成コードです。編集しないでください。",
                ).build()

        fileSpec.writeTo(System.out)
    }
}
