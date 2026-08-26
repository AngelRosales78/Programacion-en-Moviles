plugins {
    kotlin("jvm") version "2.0.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
}

sourceSets {
    main {
        kotlin {
            setSrcDirs(listOf("Semana02/SinIA/Lab02CarritoKotlin"))
        }
    }
}
