plugins {
    kotlin("jvm") version "2.0.0"
    application
}

group = "com.tfandkusu"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}
application {
    mainClass.set("com.tfandkusu.ga913yaml.MainKt")
}
