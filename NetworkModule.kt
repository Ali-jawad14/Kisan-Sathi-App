package com.example.kisansathi.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkModule {
    
    private const val BASE_URL = "https://api.openai.com/"
    
    // This is where you would put your API key
    private const val API_KEY = "YOUR_OPENAI_API_KEY_HERE"
    
    private val authInterceptor = Interceptor { chain ->
        val newRequest = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $API_KEY")
            .build()
        chain.proceed(newRequest)
    }
    
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()
    
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    val openAIService: OpenAIApiService = retrofit.create(OpenAIApiService::class.java)
}
