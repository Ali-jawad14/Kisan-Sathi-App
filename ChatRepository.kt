package com.example.kisansathi.repository

import com.example.kisansathi.network.ChatCompletionRequest
import com.example.kisansathi.network.ChatMessage
import com.example.kisansathi.network.NetworkModule

class ChatRepository {
    
    private val apiService = NetworkModule.openAIService
    
    // System prompt for agricultural chatbot in Urdu
    private val systemPrompt = """
        آپ ایک پاکستانی کسانوں کے لیے زرعی مشیر ہیں۔ آپ کا نام "کسان ساتھی" ہے۔
        آپ کو اردو میں جواب دینا ہے اور پاکستان کی آب و ہوا اور زرعی حالات کے مطابق مشورے دینے ہیں۔
        آپ کے پاس گندم، چاول، آلو، ٹماٹر، مرچ، سیب اور دیگر فصلوں کی معلومات ہیں۔
        ہمیشہ مفید، واضح اور عملی مشورے دیں۔
    """.trimIndent()
    
    suspend fun getChatResponse(userMessage: String, conversationHistory: List<Message>): String {
        return try {
            // Prepare messages for API
            val messages = mutableListOf<ChatMessage>()
            
            // Add system prompt
            messages.add(ChatMessage("system", systemPrompt))
            
            // Add conversation history
            conversationHistory.takeLast(10).forEach { message ->
                messages.add(
                    ChatMessage(
                        role = if (message.isUser) "user" else "assistant",
                        content = message.text
                    )
                )
            }
            
            // Add current user message
            messages.add(ChatMessage("user", userMessage))
            
            val request = ChatCompletionRequest(
                model = "gpt-3.5-turbo",
                messages = messages,
                max_tokens = 200,
                temperature = 0.7
            )
            
            val response = apiService.getChatCompletion(request)
            
            if (response.isSuccessful) {
                response.body()?.choices?.firstOrNull()?.message?.content 
                    ?: "معذرت، میں اس وقت جواب نہیں دے سکتا۔"
            } else {
                "نیٹ ورک کی خرابی۔ براہ کرم دوبارہ کوشش کریں۔"
            }
            
        } catch (e: Exception) {
            "خرابی: ${e.message ?: "نامعلوم خرابی"}"
        }
    }
}
