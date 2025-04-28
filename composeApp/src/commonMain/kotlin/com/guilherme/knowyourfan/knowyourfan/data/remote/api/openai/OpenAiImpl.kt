package com.guilherme.knowyourfan.knowyourfan.data.remote.api.openai

import com.guilherme.knowyourfan.knowyourfan.data.remote.api.OpenAiService
import com.guilherme.knowyourfan.knowyourfan.data.remote.api.openai.model.Content
import com.guilherme.knowyourfan.knowyourfan.data.remote.api.openai.model.InlineData
import com.guilherme.knowyourfan.knowyourfan.data.remote.api.openai.model.Part
import com.guilherme.knowyourfan.knowyourfan.data.remote.api.openai.model.RequestBody
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json

class OpenAiImpl : OpenAiService {

    companion object {
        const val GOOGLE_API_KEY = ""
    }

    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }

    override suspend fun analyzeImage(image: String, id: String) {

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

        try {
            val response: HttpResponse = client.post("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=$GOOGLE_API_KEY") {

                header(HttpHeaders.ContentType, ContentType.Application.Json)

                setBody(requestBody)
            }

            println("Status: ${response.status}")
            println("Response: ${response.bodyAsText()}")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}