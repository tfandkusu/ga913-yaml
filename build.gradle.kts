plugins {
    kotlin("jvm") version libs.versions.kotlin
    application
    alias(libs.plugins.spotless)
}

group = "com.tfandkusu"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation(libs.junit)
}

tasks.test {
    useJUnit()
}
kotlin {
    jvmToolchain(17)
}
application {
    mainClass.set("com.tfandkusu.ga913yaml.MainKt")
}
spotless {
    kotlin {
        target("**/*.kt")
        ktlint("1.3.1")
    }
}
