package com.guilherme.knowyourfan.knowyourfan.data.remote.api.gemini

import com.guilherme.knowyourfan.core.domain.GeminiError
import com.guilherme.knowyourfan.domain.Result
import com.guilherme.knowyourfan.knowyourfan.data.remote.api.gemini.model.ApiResponse
import com.guilherme.knowyourfan.knowyourfan.data.remote.firebase.model.UserInterests

interface GeminiService {

    suspend fun analyzeImage(image: String, id: String): Result<String?, GeminiError.Gemini>

    suspend fun getRecommendations(userInterests: UserInterests): Result<ApiResponse, GeminiError.Recommendations>

}