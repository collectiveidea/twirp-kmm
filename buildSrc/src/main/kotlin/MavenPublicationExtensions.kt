import org.gradle.api.publish.maven.MavenPublication

fun MavenPublication.configureTwirpKmmPOM(pomDescription: String) {
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

        url.set("https://github.com/collectiveidea/twirp-kmm")

        issueManagement {
            system.set("Github")
            url.set("https://github.com/collectiveidea/twirp-kmm/issues")
        }

        scm {
            connection.set("https://github.com/collectiveidea/twirp-kmm.git")
            url.set("https://github.com/collectiveidea/twirp-kmm")
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
