# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/), and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

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
