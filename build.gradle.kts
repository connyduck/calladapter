buildscript {
    repositories {
        gradlePluginPortal()
    }
    dependencies {
        classpath(kotlin("gradle-plugin", version = "1.6.10"))
        classpath("org.jlleitschuh.gradle:ktlint-gradle:10.2.1")
        classpath("io.github.gradle-nexus:publish-plugin:1.1.0")
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:1.6.10")
    }
}

apply(plugin = "io.github.gradle-nexus.publish-plugin")

allprojects {
    repositories {
        mavenCentral()
    }
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
}

tasks.register("clean", Delete::class.java) {
    delete(rootProject.buildDir)
}

apply(from= "${rootDir}/scripts/publish-root.gradle")
