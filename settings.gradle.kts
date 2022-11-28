pluginManagement {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }
}

rootProject.name = "twirp-kmm"

include("generator")
project(":generator").name = "twirp-kmm-generator"
