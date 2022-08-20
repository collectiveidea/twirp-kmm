# PBandK Service Generator for Twirp

The project is a [service generator plugin](https://github.com/streem/pbandk#service-code-generation) for [PBandK](https://github.com/streem/pbandk) that generates Kotlin client integration for [Twirp](https://github.com/twitchtv/twirp) services. The generated client code leverages [PBandK](https://github.com/streem/pbandk) for protobuf messages, [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization) for [JSON handling of Twirp service errors](https://twitchtv.github.io/twirp/docs/errors.html), and [KTor](https://github.com/ktorio/ktor) for HTTP. All of these choices enable the generated client code to be leveraged in [Kotlin Multiplatform Mobile](https://kotlinlang.org/lp/mobile/) projects, sharing the network integration layer with both iOS and Android native apps.

## Usage

Download the latest release, currently `0.1.0-SNAPSHOT`, and pass it to `protoc` via `pbandk`:

```bash
# Download the library to ~
cd ~/
curl -O https://repo1.maven.org/maven2/com/collectiveida/twirp/pbandk-gen-kotlin-ktor-twirp-service/0.1.0-SNAPSHOT/pbandk-gen-kotlin-ktor-twirp-service-0.1.0-SNAPSHOT.jar
```

```bash
cd ~/exampleProject
protoc --pbandk_out=kotlin_service_gen='~/pbandk-gen-kotlin-ktor-twirp-service-0.1.0-SNAPSHOT.jar|com.collectiveidea.twirp.Generator',kotlin_package=com.example.api:src/main/kotlin src/main/proto/example.proto
```

# Build

To build the library locally, run:

```bash
./gradlew build
```

This creates the versioned `.jar` file, e.g. `lib/build/libs/lib-0.1.0-SNAPSHOT.jar`

Then, the built version can be used, instead of the latest release, by supplying the path to the built `.jar`, e.g.:

```bash
protoc --pbandk_out=kotlin_service_gen='/Users/darron/Development/pbandk-gen-kotlin-ktor-twirp-service/lib/build/libs/lib-0.1.0-SNAPSHOT.jar|com.collectiveidea.twirp.Generator',kotlin_package=com.example.api:shared/src/commonMain/kotlin shared/src/commonMain/proto/example.proto
```

