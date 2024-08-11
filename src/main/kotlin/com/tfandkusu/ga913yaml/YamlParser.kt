package com.tfandkusu.ga913yaml

import com.charleskorn.kaml.Yaml
import com.tfandkusu.ga913yaml.model.Screen
import kotlinx.serialization.builtins.ListSerializer
import java.io.File

class YamlParser {
    fun parseScreens(): List<Screen> {
        val yamlString = File("events/screens.yaml").readText()
        return Yaml.default.decodeFromString(ListSerializer(Screen.serializer()), yamlString)
    }
}
