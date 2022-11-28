plugins {
    id("org.jetbrains.kotlin.jvm")
}

repositories {
    mavenCentral()
}

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
