package com.tfandkusu.ga913yaml

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("./gradlew run validate or make")
        return
    }
    val command = args[0]
    when(command) {
        "validate" -> println("Validate")
        "make" -> println("Make")
        else -> println("Unknown command")
    }
}
