# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/), and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

 * **BREAKING** Rename project from twirp-kmm to twirp-kmp. See [#16](https://github.com/collectiveidea/twirp-kmm/pull/16)
 * Generator - Ensure generator .jar artifact is built for Java 8. See [#15](https://github.com/collectiveidea/twirp-kmm/pull/15).

## [0.4.0] - 2024-09-03

### Added

 * Runtime - Add additional targets (`js`, `jvm`, etc). See [#13](https://github.com/collectiveidea/twirp-kmm/pull/13).

### Changed

 * Runtime - Enabled Explicit API mode (see [#11](https://github.com/collectiveidea/twirp-kmm/pull/11)). This
   might hide some of our internal library helpers from downstream consumers.
 * Update dependencies
   * Bump Kotlin from 1.9.21 to 2.0.20
   * Runtime - Bump kotlinx-serialization dependency from 1.6.0 to 1.7.1
   * Runtime - Bump ktor dependency from 2.3.6 to 2.3.12
   * Runtime - Bump Android Gradle Plugin from 8.1.2 to 8.5.2
   * Runtime - Bump Android Compile SDK to 34
   * Bump PBandK from 0.14.2 to 0.15.0
   * Bump gradle from 8.4 to 8.7

## [0.3.2] - 2023-12-06

### Changed

 * Bump Kotlin version from 1.8.10 to 1.9.21
 * Runtime - Bump kotlinx-serialization dependency from 1.4.1 to 1.6.0
 * Runtime - Bump ktor dependency from 2.2.3 to 2.3.6
 * Runtime - Bump Android Gradle Plugin from 7.4.1 to 8.1.2
 * Runtime - Explicitly target Java 8 compatibility.
 * Bump gradle from 7.5.1 to 8.4

## [0.3.1] - 2023-02-15

### Fixed

 * Generator - Fixed a type issue in generated service code. See [#6](https://github.com/collectiveidea/twirp-kmm/pull/6).

## [0.3.0] - 2023-02-15

### Added

 * Runtime - Added helper method to `ServiceException` for checking `304 Not Modified` server response. See [#5](https://github.com/collectiveidea/twirp-kmm/pull/5).

### Changed

 * Generator - Generated service methods now return a `Pair`, to access response headers. See [#4](https://github.com/collectiveidea/twirp-kmm/pull/4).
 * Generator - `requestHeaders` type changed from `Map<String, String>` to `Header` for consistency with `Pair` result. See [#4](https://github.com/collectiveidea/twirp-kmm/pull/4).

## [0.2.0] - 2023-02-13

### Added

 * Generator - Generated service methods now accept optional request headers. See [#2](https://github.com/collectiveidea/twirp-kmm/pull/2).

## [0.1.0] - 2022-12-05

Initial Release
