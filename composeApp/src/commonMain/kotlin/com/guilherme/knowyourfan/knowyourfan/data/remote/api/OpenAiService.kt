package com.guilherme.knowyourfan.knowyourfan.data.remote.api

interface OpenAiService {

    suspend fun analyzeImage(image: String, id: String)

}