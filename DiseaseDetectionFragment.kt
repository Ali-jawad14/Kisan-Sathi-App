package com.example.kisansathi

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.kisansathi.databinding.FragmentDiseaseDetectionBinding
import com.example.kisansathi.ml.DiseaseClassifier
import kotlinx.coroutines.launch

class DiseaseDetectionFragment : Fragment() {

    private var _binding: FragmentDiseaseDetectionBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var diseaseClassifier: DiseaseClassifier
    private var selectedImageBitmap: Bitmap? = null

    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageBitmap = result.data?.extras?.get("data") as? Bitmap
            imageBitmap?.let {
                selectedImageBitmap = it
                binding.imagePreview.setImageBitmap(it)
                binding.imagePreview.visibility = View.VISIBLE
                binding.btnAnalyze.visibility = View.VISIBLE
            }
        }
    }

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, it)
                selectedImageBitmap = bitmap
                binding.imagePreview.setImageBitmap(bitmap)
                binding.imagePreview.visibility = View.VISIBLE
                binding.btnAnalyze.visibility = View.VISIBLE
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            openCamera()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiseaseDetectionBinding.inflate(inflater, container, false)
        
        diseaseClassifier = DiseaseClassifier(requireContext())
        setupClickListeners()
        
        return binding.root
    }

    private fun setupClickListeners() {
        binding.btnCamera.setOnClickListener {
            checkCameraPermission()
        }
        
        binding.btnGallery.setOnClickListener {
            galleryLauncher.launch("image/*")
        }
        
        binding.btnAnalyze.setOnClickListener {
            selectedImageBitmap?.let { bitmap ->
                analyzeImage(bitmap)
            }
        }
    }

    private fun checkCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                openCamera()
            }
            else -> {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraLauncher.launch(intent)
    }

    private fun analyzeImage(bitmap: Bitmap) {
        binding.progressBar.visibility = View.VISIBLE
        binding.btnAnalyze.isEnabled = false
        
        lifecycleScope.launch {
            try {
                val result = diseaseClassifier.classifyImage(bitmap)
                displayResult(result)
            } catch (e: Exception) {
                binding.textResult.text = "تجزیہ میں خرابی: ${e.message}"
            } finally {
                binding.progressBar.visibility = View.GONE
                binding.btnAnalyze.isEnabled = true
            }
        }
    }

    private fun displayResult(result: DiseaseClassifier.ClassificationResult) {
        binding.textResult.visibility = View.VISIBLE
        binding.textResult.text = """
            بیماری: ${result.diseaseName}
            اعتماد: ${String.format("%.1f", result.confidence * 100)}%
            
            علاج:
            ${result.treatment}
            
            احتیاط:
            ${result.prevention}
        """.trimIndent()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
