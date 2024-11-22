[![ktlint](https://img.shields.io/badge/ktlint%20code--style-%E2%9D%A4-FF4081)](https://pinterest.github.io/ktlint/)

# PBandK Service Generator and Kotlin Multiplatform Runtime for Twirp

This project is a [service generator plugin](https://github.com/streem/pbandk#service-code-generation) for [PBandK](https://github.com/streem/pbandk) that generates Kotlin client integration for [Twirp](https://github.com/twitchtv/twirp) services. The generated client code leverages [PBandK](https://github.com/streem/pbandk) for protobuf messages, [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization) for [JSON handling of Twirp service errors](https://twitchtv.github.io/twirp/docs/errors.html), and [Ktor](https://github.com/ktorio/ktor) for HTTP. All of these choices enable the generated client code to be leveraged in [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html) projects, sharing the network integration layer with compatible platform targets.

There are two parts to this project - the [generator](./generator) itself, and the supporting [runtime](./runtime) for leveraging the generated service code.

## Generator

In general, follow [PBandK Usage](https://github.com/streem/pbandk#usage) instructions, but supply the `twirp-kmp-generator` as the `kotlin_service_gen` option as described in [PBandK's Service Code Generation documentation](https://github.com/streem/pbandk#service-code-generation).

### Usage

Download the latest release, currently `0.4.0`, and pass it to `protoc` via `pbandk`:

```bash
# Download the library to ~
cd ~/
curl -OL https://github.com/collectiveidea/twirp-kmp/releases/download/0.4.0/twirp-kmp-generator-0.4.0.jar
```


Pass the jar and generator class name as the `kotlin_service_gen` option to `pbandk_out`:

```bash
cd ~/exampleProject
protoc --pbandk_out=kotlin_service_gen='~/twirp-kmp-generator-0.4.0.jar|com.collectiveidea.twirp.Generator',kotlin_package=com.example.api:src/main/kotlin src/main/proto/example.proto
```

### Build

To build the library locally, run:

```bash
./gradlew build
```

This creates the versioned `.jar` file, e.g. `generator/build/libs/twirp-kmp-generator-0.4.0-SNAPSHOT.jar`

Then, the built version can be used, instead of the latest release, by supplying the path to the built `.jar`, e.g.:

```bash
protoc --pbandk_out=kotlin_service_gen='/Users/darron/Development/twirp-kmp/generator/build/libs/twirp-kmp-generator-0.4.0-SNAPSHOT.jar|com.collectiveidea.twirp.Generator',kotlin_package=com.example.api:shared/src/commonMain/kotlin shared/src/commonMain/proto/example.proto
```

## Runtime

The runtime provides an [`installTwirp`](./runtime/src/commonMain/kotlin/com/collectiveidea/twirp/HttpClientTwirpHelper.kt) helper to configure a Ktor HttpClient for Twirp integration.

First, add the runtime as a dependency:

```
implementation "com.collectiveidea.twirp:twirp-kmp-runtime:0.4.0"
```

Then, configure the HttpClient and pass the client into the generated service constructor:

```kotlin
val client = HttpClient(engine) {    
    installTwirp(baseUrl)
}

val exampleService = ExampleServiceImpl(client)
```

Service methods throw a [`ServiceException`](./runtime/src/commonMain/kotlin/com/collectiveidea/twirp/ServiceException.kt) on error. The `ServiceException` contains the parsed error response from the Twirp service JSON body.
