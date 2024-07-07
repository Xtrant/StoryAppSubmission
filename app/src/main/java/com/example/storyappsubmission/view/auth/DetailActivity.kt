package com.example.storyappsubmission.view.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.storyappsubmission.R
import com.example.storyappsubmission.result.Result
import com.example.storyappsubmission.data.response.Story
import com.example.storyappsubmission.databinding.ActivityDetailBinding
import com.example.storyappsubmission.view.ViewModelFactory

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    private val viewModel: MainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        observeViewModel()

        getDetailStory()
    }

    private fun getDetailStory() {
        viewModel.getToken().observe(this) {token ->
            val idStory = intent.getStringExtra(ID)
            idStory?.let { viewModel.getDetailStory(idStory, token) }
        }
    }

    private fun observeViewModel() {
        viewModel.resultDetailStory.observe(this){result ->
            when (result) {
                is Result.Loading -> {
                    showLoading(true)
                }
                is Result.Success -> {
                    val list = result.data.story
                    setDetailStory(list)
                    showLoading(false)
                }
                is Result.Error -> {
                    showToast(result.error)
                    showLoading(false)
                }
            }
        }
    }

    private fun setDetailStory(list: Story) {
        binding.tvTitle.text = list.name
        binding.tvDescription.text = list.description
        Glide.with(binding.root)
            .load(list.photoUrl)
            .into(binding.ivMain)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val ID = "id"
    }
}