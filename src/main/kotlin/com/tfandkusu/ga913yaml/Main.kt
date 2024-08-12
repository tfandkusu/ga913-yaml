package com.tfandkusu.ga913yaml

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("./gradlew run --args validate")
        println("./gradlew run --args make")
        return
    }
    val command = args[0]
    when (command) {
        "validate" -> {
            val screens = YamlParser.parse()
            Validator.validate(screens)
        }
        "make" -> {
            val screens = YamlParser.parse()
            KotlinGenerator.generate(screens)
        }
        else -> println("Unknown command")
    }
}
