@file:Suppress("SpellCheckingInspection")

group = "de.dhbw-stuttgart.hb"
version = "1.0"

plugins {
    application
}

application {
    mainClass.set("Main")
    applicationName = "sudokuh"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(16))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    val junitGroup = "org.junit.jupiter"
    val junitVersion = "5.7.2"
    testImplementation(junitGroup, "junit-jupiter-api", junitVersion)
    testImplementation(junitGroup, "junit-jupiter-params", junitVersion)
    testRuntimeOnly(junitGroup, "junit-jupiter-engine", junitVersion)
}

tasks.withType<Test> {
    useJUnitPlatform()
}
