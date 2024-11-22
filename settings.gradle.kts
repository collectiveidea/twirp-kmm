pluginManagement {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }
}

rootProject.name = "twirp-kmp"

include(":generator")
project(":generator").name = "twirp-kmp-generator"

include(":runtime")
project(":runtime").name = "twirp-kmp-runtime"
