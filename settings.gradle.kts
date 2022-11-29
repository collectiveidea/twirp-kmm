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

include(":runtime")
project(":runtime").name = "twirp-kmm-runtime"
