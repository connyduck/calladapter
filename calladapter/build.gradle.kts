import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    `maven-publish`
    signing
    id("org.jetbrains.dokka")
}

val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

val javadocJar by tasks.registering(Jar::class) {
    dependsOn(tasks.dokkaJavadoc)
    archiveClassifier.set("javadoc")
    from(tasks.dokkaJavadoc.get().outputDirectory.get())
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                groupId = "at.connyduck"
                artifactId = "kotlin-result-calladapter"
                version = "1.0.0"

                from(components["java"])
                artifact(sourcesJar.get())
                artifact(javadocJar.get())

                pom {
                    name.set("kotlin-result-calladapter")
                    description.set("A Retrofit calladapter that allows to have Kotlin's Result as a return type")
                    url.set("https://github.com/connyduck/calladapter")

                    licenses {
                        license {
                            name.set("Apache-2.0 License")
                            url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }

                    developers {
                        developer {
                            id.set("connyduck")
                            name.set("Konrad Pozniak")
                            email.set("opensource@connyduck.at")
                        }
                    }

                    scm {
                        connection.set("scm:git:github.com/connyduck/calladapter.git")
                        developerConnection.set("scm:git:ssh://github.com/connyduck/calladapter.git")
                        url.set("https://github.com/connyduck/calladapter/tree/main")
                    }
                }
            }
        }
    }
}

signing {
    val signingKeyId = rootProject.ext["signing.keyId"] as String?
    val signingKey = rootProject.ext["signing.key"] as String?
    val signingPassword = rootProject.ext["signing.password"] as String?
    useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
    sign(publishing.publications)
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
