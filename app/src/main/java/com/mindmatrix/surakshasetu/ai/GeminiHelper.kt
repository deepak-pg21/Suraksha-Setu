package com.mindmatrix.surakshasetu.ai

import com.google.ai.client.generativeai.GenerativeModel
import com.mindmatrix.surakshasetu.BuildConfig

object GeminiHelper {

    val model = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.GEMINI_API_KEY
    )
}