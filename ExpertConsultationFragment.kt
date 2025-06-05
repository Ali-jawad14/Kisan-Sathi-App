package com.example.kisansathi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kisansathi.adapters.ExpertAdapter
import com.example.kisansathi.databinding.FragmentExpertConsultationBinding
import com.example.kisansathi.models.Expert
import com.example.kisansathi.network.ExpertRepository
import kotlinx.coroutines.launch

class ExpertConsultationFragment : Fragment() {

    private var _binding: FragmentExpertConsultationBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var expertAdapter: ExpertAdapter
    private val expertRepository = ExpertRepository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExpertConsultationBinding.inflate(inflater, container, false)
        
        setupRecyclerView()
        loadExperts()
        setupClickListeners()
        
        return binding.root
    }

    private fun setupRecyclerView() {
        expertAdapter = ExpertAdapter { expert ->
            // Handle expert selection
            showExpertDetails(expert)
        }
        binding.recyclerExperts.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerExperts.adapter = expertAdapter
    }

    private fun setupClickListeners() {
        binding.btnBookConsultation.setOnClickListener {
            bookConsultation()
        }
        
        binding.chipCrops.setOnClickListener {
            filterExperts("crops")
        }
        
        binding.chipLivestock.setOnClickListener {
            filterExperts("livestock")
        }
        
        binding.chipSoil.setOnClickListener {
            filterExperts("soil")
        }
        
        binding.chipPestControl.setOnClickListener {
            filterExperts("pest_control")
        }
    }

    private fun loadExperts() {
        lifecycleScope.launch {
            try {
                binding.progressBar.visibility = View.VISIBLE
                val experts = expertRepository.getExperts()
                expertAdapter.updateExperts(experts)
            } catch (e: Exception) {
                binding.textError.visibility = View.VISIBLE
                binding.textError.text = "ماہرین کی فہرست لوڈ نہیں ہو سکی"
            } finally {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun filterExperts(category: String) {
        lifecycleScope.launch {
            try {
                val experts = expertRepository.getExpertsByCategory(category)
                expertAdapter.updateExperts(experts)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    private fun showExpertDetails(expert: Expert) {
        binding.apply {
            cardSelectedExpert.visibility = View.VISIBLE
            textExpertName.text = expert.name
            textExpertSpecialty.text = expert.specialty
            textExpertExperience.text = "${expert.experience} سال تجربہ"
            textExpertRating.text = "ریٹنگ: ${expert.rating}/5"
            textExpertFee.text = "فیس: ${expert.consultationFee} روپے"
        }
    }

    private fun bookConsultation() {
        // Implement consultation booking logic
        binding.textBookingStatus.visibility = View.VISIBLE
        binding.textBookingStatus.text = "آپ کی درخواست بھیج دی گئی ہے۔ ماہر جلد آپ سے رابطہ کریں گے۔"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
