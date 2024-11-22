# Releasing

1. Update `version` in `gradle.properties` to the release version.

2. Update the `CHANGELOG.md`.

3. Update the `README.md` to reflect the new release version number.

4. Commit

   ```
   $ git commit -am "Prepare version X.Y.Z"
   ```

5. Tag

   ```
   $ git tag -am "Version X.Y.Z" X.Y.Z
   ```

6. Push!

   ```
   $ git push && git push --tags
   ```

7. Build (generator)

   ```
   $ ./gradlew build
   ```

8. Create GitHub Release
   1. Visit the [New Releases](https://github.com/collectiveidea/twirp-kmp/releases/new) page.
   2. Supply release version and changelog
   3. Upload `generator/build/libs/twirp-kmp-generator-X.Y.Z.jar` artifact.

9. Publish (runtime)

   ```
   $ ./gradlew publish
   ```

10. Visit [Sonatype Nexus](https://s01.oss.sonatype.org) and promote the artifact.
