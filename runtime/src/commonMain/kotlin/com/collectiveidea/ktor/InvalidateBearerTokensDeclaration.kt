package com.collectiveidea.ktor

import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.plugins.plugin

// Force the Auth plugin to invoke the `loadTokens` block again.
// See: https://stackoverflow.com/q/72064782 and https://stackoverflow.com/a/67316630
// and https://youtrack.jetbrains.com/issue/KTOR-4759/Auth-BearerAuthProvider-caches-result-of-loadToken-until-process-death
fun HttpClient.invalidateBearerTokens() {
    try {
        plugin(Auth).providers
            .filterIsInstance<BearerAuthProvider>()
            .first().clearToken()
    } catch (e: IllegalStateException) {
        // No-op; plugin not installed
    }
}
