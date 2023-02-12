package com.collectiveidea.twirp

import pbandk.gen.ServiceGenerator
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

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
                suspend fun ${kotlinServiceMethodName}(request: $reqType, requestHeaders: Map<String, String>? = null): $respType
             """.trimIndent()

            implementationMethods += """
                override suspend fun ${kotlinServiceMethodName}(request: $reqType, requestHeaders: Map<String, String>?): $respType {
                    val response: HttpResponse = httpClient.post("${service.file.packageName}.${service.name}/${method.name}") {
                        requestHeaders?.forEach { headers.append(it.key, it.value) }
                        setBody(request.encodeToByteArray())
                    }
                    return $respType.decodeFromByteArray(response.body())
                }
            """.trimIndent()
        }

        return listOf(
            interfaceKt(service.file.kotlinPackageName, service.name, filePath, interfaceMethods),
            implementationKt(
                service.file.kotlinPackageName,
                service.name,
                filePath,
                implementationMethods
            )
        )
    }

    private fun repeatMethods(methods: List<String>): String {
        // For each method...
        return methods.map {
                // For each line...
                it.split("\n")
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
        interfaceMethods: MutableList<String>
    ) = ServiceGenerator.Result(
        otherFilePath = filePath.resolveSibling("${serviceName}.kt").toString(),
        code = """
            package $kotlinPackageName
            
            import com.collectiveidea.twirp.ServiceException
            import kotlin.coroutines.cancellation.CancellationException
            
            interface $serviceName {
                ${repeatMethods(interfaceMethods)}
            }${'\n'}
""".trimIndent()
    )

    private fun implementationKt(
        kotlinPackageName: String?,
        serviceName: String,
        filePath: Path,
        implementationMethods: MutableList<String>
    ) = ServiceGenerator.Result(
        otherFilePath = filePath.resolveSibling("${serviceName}Impl.kt").toString(),
        code = """
            package $kotlinPackageName
            
            import io.ktor.client.HttpClient
            import io.ktor.client.call.body
            import io.ktor.client.request.post
            import io.ktor.client.request.setBody
            import io.ktor.client.statement.HttpResponse
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
        """.trimIndent()
    )

    private fun String.lowercasedFirstLetter(): String {
        return when (this.length) {
            0 -> ""
            1 -> this.lowercase(Locale.getDefault())
            else -> this[0].lowercase(Locale.getDefault()) + this.substring(1)
        }
    }
}
