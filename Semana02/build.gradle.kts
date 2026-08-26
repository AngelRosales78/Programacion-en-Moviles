plugins {
    kotlin("jvm") version "2.4.10"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

// Iguala las versiones de Java y Kotlin exactamente en 17
java {
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}
dependencies {
    implementation(kotlin("stdlib-jdk8"))
}
kotlin {
    jvmToolchain(17)
}

// Le enseña a Gradle dónde están tus archivos sin mover carpetas
sourceSets {
    main {
        kotlin.srcDirs("ConIA", "SinIA")
    }
}

application {
    mainClass.set("conia.CarritoKt")
}

tasks.withType<JavaExec> {
    standardInput = System.`in`
}
