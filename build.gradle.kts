plugins {
    kotlin("jvm") version "2.0.0"
    application
}

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
    jvmToolchain(21)
}

sourceSets {
    main {
        kotlin.srcDirs(
            "Semana02/SinIA/Lab02CarritoKotlin",
            "Semana02/ConIA/Lab02CarritoKotlin"
        )
    }
}