package com.example.kisansathi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.kisansathi.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        
        setupClickListeners()
        
        return binding.root
    }

    private fun setupClickListeners() {
        binding.cardChatbot.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_chatbot)
        }
        
        binding.cardDiseaseDetection.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_disease_detection)
        }
        
        binding.cardWeatherMarket.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_weather_market)
        }
        
        binding.cardExpertConsultation.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_expert_consultation)
        }
        
        binding.cardCommunity.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_community)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
