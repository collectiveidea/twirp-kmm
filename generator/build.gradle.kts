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
    compileOnly("pro.streem.pbandk:pbandk-runtime:0.14.1")
    compileOnly("pro.streem.pbandk:protoc-gen-pbandk-lib:0.14.1")

    // Running the test requires `pbandk.gen.ServiceGenerator`, but the library
    // itself only needs it for compile-time. See README.md#Usage for details.
    testImplementation("pro.streem.pbandk:protoc-gen-pbandk-lib:0.14.1")

    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
}

tasks.jar {
    manifest {
        attributes(
            mapOf(
                "Implementation-Title" to project.name,
                "Implementation-Version" to project.version
            )
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
