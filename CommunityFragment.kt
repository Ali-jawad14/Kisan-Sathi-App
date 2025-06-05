package com.example.kisansathi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kisansathi.adapters.CommunityPostAdapter
import com.example.kisansathi.databinding.FragmentCommunityBinding
import com.example.kisansathi.models.CommunityPost
import com.example.kisansathi.network.CommunityRepository
import kotlinx.coroutines.launch

class CommunityFragment : Fragment() {

    private var _binding: FragmentCommunityBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var postAdapter: CommunityPostAdapter
    private val communityRepository = CommunityRepository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommunityBinding.inflate(inflater, container, false)
        
        setupRecyclerView()
        loadPosts()
        setupClickListeners()
        
        return binding.root
    }

    private fun setupRecyclerView() {
        postAdapter = CommunityPostAdapter(
            onLikeClick = { post -> likePost(post) },
            onCommentClick = { post -> showComments(post) },
            onShareClick = { post -> sharePost(post) }
        )
        binding.recyclerPosts.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerPosts.adapter = postAdapter
    }

    private fun setupClickListeners() {
        binding.fabCreatePost.setOnClickListener {
            showCreatePostDialog()
        }
        
        binding.btnPost.setOnClickListener {
            createPost()
        }
    }

    private fun loadPosts() {
        lifecycleScope.launch {
            try {
                binding.progressBar.visibility = View.VISIBLE
                val posts = communityRepository.getCommunityPosts()
                postAdapter.updatePosts(posts)
            } catch (e: Exception) {
                binding.textError.visibility = View.VISIBLE
                binding.textError.text = "کمیونٹی پوسٹس لوڈ نہیں ہو سکیں"
            } finally {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun showCreatePostDialog() {
        binding.cardCreatePost.visibility = View.VISIBLE
    }

    private fun createPost() {
        val content = binding.editPostContent.text.toString().trim()
        if (content.isNotEmpty()) {
            lifecycleScope.launch {
                try {
                    val newPost = communityRepository.createPost(content)
                    postAdapter.addPost(newPost)
                    binding.editPostContent.text.clear()
                    binding.cardCreatePost.visibility = View.GONE
                } catch (e: Exception) {
                    // Handle error
                }
            }
        }
    }

    private fun likePost(post: CommunityPost) {
        lifecycleScope.launch {
            try {
                communityRepository.likePost(post.id)
                // Update UI
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    private fun showComments(post: CommunityPost) {
        // Navigate to comments screen or show dialog
    }

    private fun sharePost(post: CommunityPost) {
        // Implement sharing functionality
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
