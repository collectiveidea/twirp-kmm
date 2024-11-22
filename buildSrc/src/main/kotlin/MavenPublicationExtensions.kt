import org.gradle.api.publish.maven.MavenPublication

fun MavenPublication.configureTwirpKmpPOM(pomDescription: String) {
    val pomName = artifactId

    pom {
        name.set(pomName)
        description.set(pomDescription)

        licenses {
            license {
                name.set("MIT")
                url.set("https://opensource.org/licenses/MIT")
            }
        }

        url.set("https://github.com/collectiveidea/twirp-kmp")

        issueManagement {
            system.set("Github")
            url.set("https://github.com/collectiveidea/twirp-kmp/issues")
        }

        scm {
            connection.set("https://github.com/collectiveidea/twirp-kmp.git")
            url.set("https://github.com/collectiveidea/twirp-kmp")
        }

        developers {
            developer {
                id.set("collectiveidea")
                name.set("Collective Idea")
                url.set("https://github.com/collectiveidea")
            }
        }
    }
}
