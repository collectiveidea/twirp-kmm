import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("org.jetbrains.kotlin.jvm")
    `maven-publish`
    signing
}

repositories {
    mavenCentral()
}

description = "Twirp service generator PBandK plugin for use in Kotlin Multiplatform Mobile projects."

dependencies {
    compileOnly(libs.pbandk.runtime)
    compileOnly(libs.pbandk.protoc.gen)

    // Running the test requires `pbandk.gen.ServiceGenerator`, but the library
    // itself only needs it for compile-time. See README.md#Usage for details.
    testImplementation(libs.pbandk.protoc.gen)

    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlin.test.junit)
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile>().configureEach {
    compilerOptions.jvmTarget.set(JvmTarget.JVM_1_8)
}

tasks.jar {
    manifest {
        attributes(
            mapOf(
                "Implementation-Title" to project.name,
                "Implementation-Version" to project.version,
            ),
        )
    }
}

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
}

publishing {
    publications.withType<MavenPublication> {
        artifact(javadocJar.get())

        configureTwirpKmmPOM(project.description!!)
    }
}
