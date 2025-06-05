package com.example.kisansathi

data class Message(
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)
