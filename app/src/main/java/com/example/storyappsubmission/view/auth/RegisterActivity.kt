package com.example.storyappsubmission.view.auth

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.storyappsubmission.R
import com.example.storyappsubmission.result.Result
import com.example.storyappsubmission.databinding.ActivityRegisterBinding
import com.example.storyappsubmission.view.ViewModelFactory


class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: MainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        playAnimation()

        setMyButtonEnable()

        observeViewModel()

        binding.etName.addTextChangedListener(MyTextWatcher())
        binding.etEmail.addTextChangedListener(MyTextWatcher())
        binding.etPass.addTextChangedListener(MyTextWatcher())

        binding.customBtn.setOnClickListener {
            register()
        }

        binding.tvClickable.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun register() {
        val email = binding.etEmail.text.toString().trim()
        val name = binding.etName.text.toString().trim()
        val password = binding.etPass.text.toString().trim()

        viewModel.register(email, name, password)
    }

    private fun observeViewModel() {
        viewModel.resultMessage.observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        showLoading(true)
                    }

                    is Result.Success -> {
                        showToast(result.data.message)
                        showLoading(false)
                        startActivity(Intent(this, LoginActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        })
                    }

                    is Result.Error -> {
                        showToast(result.error)
                        showLoading(false)
                    }
                }
            }
        }
    }

    private fun setMyButtonEnable() {
        val etEmail = binding.etEmail.text
        val etName = binding.etName.text
        val etPassword = binding.etPass.text
        binding.customBtn.isEnabled = etEmail != null && etEmail.toString()
            .isNotEmpty() && etName != null && etName.toString()
            .isNotEmpty() && etPassword != null && etPassword.toString()
            .isNotEmpty() && etPassword.toString().length >= 8 && Patterns.EMAIL_ADDRESS.matcher(
            etEmail
        ).matches()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.ivMain, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val titleEmail =
            ObjectAnimator.ofFloat(binding.tvTitleEmail, View.ALPHA, 1f).setDuration(200)
        val editTextEmail = ObjectAnimator.ofFloat(binding.etEmail, View.ALPHA, 1f).setDuration(200)
        val titleName = ObjectAnimator.ofFloat(binding.tvTitleName, View.ALPHA, 1f).setDuration(200)
        val editTextName = ObjectAnimator.ofFloat(binding.etName, View.ALPHA, 1f).setDuration(200)
        val titlePass =
            ObjectAnimator.ofFloat(binding.tvTitlePassword, View.ALPHA, 1f).setDuration(200)
        val editTextPass = ObjectAnimator.ofFloat(binding.etPass, View.ALPHA, 1f).setDuration(200)
        val customButton =
            ObjectAnimator.ofFloat(binding.customBtn, View.ALPHA, 1f).setDuration(200)
        val instruction =
            ObjectAnimator.ofFloat(binding.tvInstruction, View.ALPHA, 1f).setDuration(200)
        val instructionLink =
            ObjectAnimator.ofFloat(binding.tvClickable, View.ALPHA, 1f).setDuration(200)

        val instructionTogether = AnimatorSet().apply {
            playTogether(instruction, instructionLink)
        }

        AnimatorSet().apply {
            playSequentially(
                titleEmail,
                editTextEmail,
                titleName,
                editTextName,
                titlePass,
                editTextPass,
                customButton,
                instructionTogether
            )
            start()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    inner class MyTextWatcher : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            setMyButtonEnable()
        }

        override fun afterTextChanged(s: Editable?) {
        }
    }
}