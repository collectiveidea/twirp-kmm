package com.collectiveidea.ktor

import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.authProvider
import io.ktor.client.plugins.auth.providers.BearerAuthProvider

// Force the Auth plugin to invoke the `loadTokens` block again.
// See: https://stackoverflow.com/q/72064782 and https://stackoverflow.com/a/67316630
// and https://youtrack.jetbrains.com/issue/KTOR-4759/Auth-BearerAuthProvider-caches-result-of-loadToken-until-process-death
public fun HttpClient.invalidateBearerTokens() {
    authProvider<BearerAuthProvider>()?.clearToken()
}
