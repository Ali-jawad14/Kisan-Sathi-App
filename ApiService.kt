package com.example.kisansathi.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface OpenAIApiService {
    @Headers("Content-Type: application/json")
    @POST("v1/chat/completions")
    suspend fun getChatCompletion(
        @Body request: ChatCompletionRequest
    ): Response<ChatCompletionResponse>
}

data class ChatCompletionRequest(
    val model: String = "gpt-3.5-turbo",
    val messages: List<ChatMessage>,
    val max_tokens: Int = 150,
    val temperature: Double = 0.7
)

data class ChatMessage(
    val role: String, // "user" or "assistant" or "system"
    val content: String
)

data class ChatCompletionResponse(
    val choices: List<Choice>
)

data class Choice(
    val message: ChatMessage
)
