package com.guilherme.knowyourfan.knowyourfan.data.remote.api.gemini

import com.guilherme.knowyourfan.core.domain.GeminiError
import com.guilherme.knowyourfan.domain.Result
import com.guilherme.knowyourfan.knowyourfan.data.remote.api.gemini.model.ApiResponse
import com.guilherme.knowyourfan.knowyourfan.data.remote.api.gemini.model.Content
import com.guilherme.knowyourfan.knowyourfan.data.remote.api.gemini.model.InlineData
import com.guilherme.knowyourfan.knowyourfan.data.remote.api.gemini.model.Part
import com.guilherme.knowyourfan.knowyourfan.data.remote.api.gemini.model.RequestBody
import com.guilherme.knowyourfan.knowyourfan.data.remote.api.gemini.model.Response
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.putJsonObject

class GeminiImpl : GeminiService {

    companion object {
        const val GEMINI_ENDPOINT =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key="
        const val GOOGLE_API_KEY = "AIzaSyBaO_K7pbsAeycoi6Z5JhCLkqY2fKPPLUM"
    }

    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    override suspend fun analyzeImage(
        image: String,
        id: String,
    ): Result<String?, GeminiError.Gemini> {

        val requestBody = RequestBody(
            contents = listOf(
                Content(
                    parts = listOf(
                        Part(
                            inlineData = InlineData(
                                mimeType = "image/jpeg",
                                data = image
                            )
                        ),
                        Part(
                            text = "Analise a imagem do documento anexada. Localize e retorne true ou false se for igual o número do CPF (Cadastro de Pessoas Físicas) $id . O formato esperado é XXX.XXX.XXX-XX ou XXXXXXXXXXX"
                        )
                    )
                )
            )
        )

        return try {
            val response: HttpResponse =
                client.post(GEMINI_ENDPOINT + GOOGLE_API_KEY) {

                    header(HttpHeaders.ContentType, ContentType.Application.Json)

                    setBody(requestBody)
                }

            val apiResponse: Response = response.body()

            val foo = apiResponse.candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text

            Result.Success(foo)

        } catch (e: Exception) {
            val error = when (e) {
                is SerializationException -> GeminiError.Gemini.SERIALIZATION
                is RedirectResponseException -> GeminiError.Gemini.REDIRECT_RESPONSE
                is ClientRequestException -> GeminiError.Gemini.CLIENT_REQUEST
                is ServerResponseException -> GeminiError.Gemini.SERVER_RESPONSE
                is UnresolvedAddressException -> GeminiError.Gemini.UNRESOLVED_ADDRESS
                is IOException -> GeminiError.Gemini.IO
                else -> GeminiError.Gemini.UNKNOWN
            }

            Result.Error(error)
        } finally {
            client.close()
        }
    }

    override suspend fun getRecommendations(): Result<ApiResponse, GeminiError.Recommendations> {

        val prompt =
            "Busque conteúdos que sejam relevantes para este perfil de usuário tendo como contexto o mundo dos e-sports. Jogos de interesse: Counter-Strike, Valorant. Eventos que participou(presenciais e online): Major Rio 2022. Compras no último ano: Jersey Furia. Retorne notícias recentes com um header e link para cada notícia"

        val requestBody = buildJsonObject {
            put("contents", buildJsonArray {
                add(buildJsonObject {
                    put("parts", buildJsonArray {
                        add(buildJsonObject {
                            put("text", JsonPrimitive(prompt))
                        })
                    })
                })
            })

            put("tools", buildJsonArray {
                add(buildJsonObject {
                    putJsonObject("google_search") {}
                })
            })
        }

        return try {

            val response = client.post(GEMINI_ENDPOINT + GOOGLE_API_KEY) {

                header(HttpHeaders.ContentType, ContentType.Application.Json)

                setBody(requestBody)

            }

            println(response.bodyAsText())

            when (response.status) {
                HttpStatusCode.OK -> Result.Success(response.body())
                HttpStatusCode.BadRequest -> Result.Error(GeminiError.Recommendations.BAD_REQUEST)
                HttpStatusCode.Unauthorized -> Result.Error(GeminiError.Recommendations.UNAUTHORIZED)
                HttpStatusCode.InternalServerError -> Result.Error(GeminiError.Recommendations.SERVER_ERROR)
                HttpStatusCode.ServiceUnavailable -> Result.Error(GeminiError.Recommendations.SERVICE_UNAVAILABLE)
                else -> Result.Error(GeminiError.Recommendations.UNKNOWN)
            }

        } catch (e: Exception) {
            val error = when (e) {
                else -> GeminiError.Recommendations.UNKNOWN
            }
            Result.Error(error)
        }
    }


}