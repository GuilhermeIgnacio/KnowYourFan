package com.guilherme.knowyourfan.knowyourfan.data.remote.api.gemini

import com.guilherme.knowyourfan.core.domain.GeminiError
import com.guilherme.knowyourfan.domain.Result
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
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

class GeminiImpl : GeminiService {

    companion object {
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
                client.post("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=$GOOGLE_API_KEY") {

                    header(HttpHeaders.ContentType, ContentType.Application.Json)

                    setBody(requestBody)
                }

            val apiResponse: Response = response.body()

            val foo = apiResponse.candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text

            Result.Success(foo)

        } catch (e: Exception) {
            val error = when(e) {
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


}