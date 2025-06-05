package com.example.kisansathi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kisansathi.databinding.FragmentChatBinding
import com.example.kisansathi.repository.ChatRepository
import kotlinx.coroutines.launch

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private val messages = mutableListOf<Message>()
    private lateinit var adapter: MessageAdapter
    private val chatRepository = ChatRepository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)

        setupRecyclerView()
        setupClickListeners()
        addWelcomeMessage()

        return binding.root
    }

    private fun setupRecyclerView() {
        adapter = MessageAdapter(messages)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    private fun setupClickListeners() {
        binding.sendButton.setOnClickListener {
            val userMessage = binding.messageInput.text.toString().trim()
            if (userMessage.isNotEmpty()) {
                sendMessage(userMessage)
            }
        }

        // Suggested questions remain the same
        binding.btnQuestion1.setOnClickListener {
            sendMessage("گندم کی بہترین اقسام کون سی ہیں؟")
        }
        // ... other suggested questions
    }

    private fun addWelcomeMessage() {
        addMessage("السلام علیکم! میں کسان ساتھی چیٹ بوٹ ہوں۔ آپ کی زراعت سے متعلق کیسے مدد کر سکتا ہوں؟", isUser = false)
    }

    private fun sendMessage(userMessage: String) {
        addMessage(userMessage, isUser = true)
        binding.messageInput.text.clear()

        // Show typing indicator
        showTypingIndicator()

        // Get AI response
        lifecycleScope.launch {
            try {
                val response = chatRepository.getChatResponse(userMessage, messages)
                hideTypingIndicator()
                addMessage(response, isUser = false)
            } catch (e: Exception) {
                hideTypingIndicator()
                addMessage("معذرت، کچھ خرابی ہوئی ہے۔ براہ کرم دوبارہ کوشش کریں۔", isUser = false)
            }
        }
    }

    private fun showTypingIndicator() {
        // Add typing indicator logic
        binding.sendButton.isEnabled = false
    }

    private fun hideTypingIndicator() {
        // Remove typing indicator logic
        binding.sendButton.isEnabled = true
    }

    private fun addMessage(text: String, isUser: Boolean) {
        messages.add(Message(text, isUser))
        adapter.notifyItemInserted(messages.size - 1)
        binding.recyclerView.scrollToPosition(messages.size - 1)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
