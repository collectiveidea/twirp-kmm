# PBandK Service Generator and Runtime for Twirp KMM

This project is a [service generator plugin](https://github.com/streem/pbandk#service-code-generation) for [PBandK](https://github.com/streem/pbandk) that generates Kotlin client integration for [Twirp](https://github.com/twitchtv/twirp) services. The generated client code leverages [PBandK](https://github.com/streem/pbandk) for protobuf messages, [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization) for [JSON handling of Twirp service errors](https://twitchtv.github.io/twirp/docs/errors.html), and [Ktor](https://github.com/ktorio/ktor) for HTTP. All of these choices enable the generated client code to be leveraged in [Kotlin Multiplatform Mobile](https://kotlinlang.org/lp/mobile/) projects, sharing the network integration layer with both iOS and Android native apps.

There are two parts to this project - the [generator](./generator) itself, and the supporting [runtime](./runtime) for leveraging the generated service code.

## Generator

In general, follow [PBandK Usage](https://github.com/streem/pbandk#usage) instructions, but supply the `twirp-kmm-generator` as the `kotlin_service_gen` option as described in [PBandK's Service Code Generation documentation](https://github.com/streem/pbandk#service-code-generation).

### Usage

Download the latest release, currently `0.3.2`, and pass it to `protoc` via `pbandk`:

```bash
# Download the library to ~
cd ~/
curl -OL https://github.com/collectiveidea/twirp-kmm/releases/download/0.3.2/twirp-kmm-generator-0.3.2.jar
```


Pass the jar and generator class name as the `kotlin_service_gen` option to `pbandk_out`:

```bash
cd ~/exampleProject
protoc --pbandk_out=kotlin_service_gen='~/twirp-kmm-generator-0.3.2.jar|com.collectiveidea.twirp.Generator',kotlin_package=com.example.api:src/main/kotlin src/main/proto/example.proto
```

### Build

To build the library locally, run:

```bash
./gradlew build
```

This creates the versioned `.jar` file, e.g. `generator/build/libs/twirp-kmm-generator-0.3.2-SNAPSHOT.jar`

Then, the built version can be used, instead of the latest release, by supplying the path to the built `.jar`, e.g.:

```bash
protoc --pbandk_out=kotlin_service_gen='/Users/darron/Development/twirp-kmm/generator/build/libs/twirp-kmm-generator-0.3.2-SNAPSHOT.jar|com.collectiveidea.twirp.Generator',kotlin_package=com.example.api:shared/src/commonMain/kotlin shared/src/commonMain/proto/example.proto
```

## Runtime

The runtime provides an [`installTwirp`](./runtime/src/commonMain/kotlin/com/collectiveidea/twirp/HttpClientTwirpHelper.kt) helper to configure a Ktor HttpClient for Twirp integration.

First, add the runtime as a dependency:

```
implementation "com.collectiveidea.twirp:twirp-kmm-runtime:0.3.2"
```

Then, configure the HttpClient and pass the client into the generated service constructor:

```kotlin
val client = HttpClient(engine) {    
    installTwirp(baseUrl)
}

val exampleService = ExampleServiceImpl(client)
```

Service methods throw a [`ServiceException`](./runtime/src/commonMain/kotlin/com/collectiveidea/twirp/ServiceException.kt) on error. The `ServiceException` contains the parsed error response from the Twirp service JSON body.
