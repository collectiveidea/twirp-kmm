package com.collectiveidea.twirp

import pbandk.gen.ServiceGenerator
import java.nio.file.Path
import java.nio.file.Paths
import java.util.Locale

var firstRun = true

class Generator : ServiceGenerator {
    override fun generate(service: ServiceGenerator.Service): List<ServiceGenerator.Result> {
        service.debug { "Generating code for service ${service.name}" }

        val filePath: Path = Paths.get(service.filePath)

        val interfaceMethods = emptyList<String>().toMutableList()
        val implementationMethods = emptyList<String>().toMutableList()
        service.methods.forEach { method ->
            val reqType = service.kotlinTypeMappings[method.inputType!!]!!
            val respType = service.kotlinTypeMappings[method.outputType!!]!!
            val kotlinServiceMethodName = method.name.lowercasedFirstLetter()

            interfaceMethods += """
                @Throws(ServiceException::class, CancellationException::class)
                suspend fun $kotlinServiceMethodName(request: $reqType, requestHeaders: Headers? = null): Pair<$respType, Headers>
            """.trimIndent()

            implementationMethods += """
                override suspend fun $kotlinServiceMethodName(request: $reqType, requestHeaders: Headers?): Pair<$respType, Headers> {
                    val response: HttpResponse = httpClient.post("${service.file.packageName}.${service.name}/${method.name}") {
                        requestHeaders?.let { headers.appendAll(it) }
                        setBody(request.encodeToByteArray())
                    }
                    return Pair($respType.decodeFromByteArray(response.body()), response.headers)
                }
            """.trimIndent()
        }

        return listOf(
            interfaceKt(service.file.kotlinPackageName, service.name, filePath, interfaceMethods),
            implementationKt(
                service.file.kotlinPackageName,
                service.name,
                filePath,
                implementationMethods,
            ),
        )
    }

    private fun repeatMethods(methods: List<String>): String {
        return methods
            // For each method...
            .map {
                it
                    // For each line...
                    .split("\n")
                    // .. add the appropriate spacing indentation in front of the line
                    .joinToString("\n                ")
            }.map {
                // Add a new line after each method
                it + "\n"
            }
            // Add a blank line and indent the start of each method
            .joinToString("\n                ")
            .dropLast(1) // Drop trailing newline after the last method
    }

    private fun interfaceKt(
        kotlinPackageName: String?,
        serviceName: String,
        filePath: Path,
        interfaceMethods: MutableList<String>,
    ) = ServiceGenerator.Result(
        otherFilePath = filePath.resolveSibling("$serviceName.kt").toString(),
        code = """
            package $kotlinPackageName
            
            import com.collectiveidea.twirp.ServiceException
            import io.ktor.http.Headers
            import kotlin.coroutines.cancellation.CancellationException
            
            interface $serviceName {
                ${repeatMethods(interfaceMethods)}
            }${'\n'}
        """.trimIndent(),
    )

    private fun implementationKt(
        kotlinPackageName: String?,
        serviceName: String,
        filePath: Path,
        implementationMethods: MutableList<String>,
    ) = ServiceGenerator.Result(
        otherFilePath = filePath.resolveSibling("${serviceName}Impl.kt").toString(),
        code = """
            package $kotlinPackageName
            
            import io.ktor.client.HttpClient
            import io.ktor.client.call.body
            import io.ktor.client.request.post
            import io.ktor.client.request.setBody
            import io.ktor.client.statement.HttpResponse
            import io.ktor.http.Headers
            import pbandk.decodeFromByteArray
            import pbandk.encodeToByteArray
            
            /**
             * @param httpClient A pre-configured Twirp-ready HttpClient. Use the
             *   [com.collectiveidea.twirp.installTwirp] helper for easy client creation, e.g.
             *
             *   val client = HttpClient(engine) {
             *      installTwirp(baseUrl)
             *   }
             */
            class ${serviceName}Impl(val httpClient: HttpClient) : $serviceName {
                ${repeatMethods(implementationMethods)}
            }${'\n'}
        """.trimIndent(),
    )

    private fun String.lowercasedFirstLetter(): String = when (this.length) {
        0 -> ""
        1 -> this.lowercase(Locale.getDefault())
        else -> this[0].lowercase(Locale.getDefault()) + this.substring(1)
    }
}
