import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    `maven-publish`
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "at.connyduck"
            artifactId = "kotlin-result-calladapter"
            version = "1.0.0"

            from(components["java"])
        }
    }
}

val compileKotlin: KotlinCompile by tasks

compileKotlin.kotlinOptions.jvmTarget = "1.8"

kotlin {
    explicitApi()
}

tasks.test {
    useJUnitPlatform()
}

dependencies {

    val okHttpVersion = "4.9.3"
    val retrofitVersion = "2.9.0"
    val jUnitVersion = "5.8.2"
    val moshiVersion = "1.13.0"

    api(kotlin("stdlib"))

    api("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.6.0")

    api("com.squareup.okhttp3:okhttp:$okHttpVersion")

    api("com.squareup.retrofit2:retrofit:$retrofitVersion")

    testImplementation("com.squareup.okhttp3:mockwebserver:$okHttpVersion")

    testImplementation("org.junit.jupiter:junit-jupiter-api:$jUnitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$jUnitVersion")

    testImplementation("com.squareup.moshi:moshi-kotlin:$moshiVersion")
    testImplementation("com.squareup.retrofit2:converter-moshi:$retrofitVersion")
}
