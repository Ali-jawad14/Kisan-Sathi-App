package com.example.kisansathi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.kisansathi.databinding.FragmentChatbotPreviewBinding

class ChatBotPreviewFragment : Fragment() {

    private var _binding: FragmentChatbotPreviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatbotPreviewBinding.inflate(inflater, container, false)

        setupClickListeners()

        return binding.root
    }

    private fun setupClickListeners() {
        // Main button to start chat
        binding.btnUseChatbot.setOnClickListener {
            findNavController().navigate(R.id.action_chatbotPreviewFragment_to_chatFragment)
        }
        
        // Preview send button (non-functional, just for demo)
        binding.btnSend.setOnClickListener {
            // This is just for preview, actual functionality is in ChatFragment
            findNavController().navigate(R.id.action_chatbotPreviewFragment_to_chatFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
