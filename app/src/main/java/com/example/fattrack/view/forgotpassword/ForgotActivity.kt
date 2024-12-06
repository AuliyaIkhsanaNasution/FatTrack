package com.example.fattrack.view.forgotpassword

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.animation.AnimatorSet
import android.os.Build
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fattrack.databinding.ActivityForgotBinding // Auto-generated binding class
import com.example.fattrack.view.login.LoginActivity

class ForgotActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgotBinding // Declare binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize View Binding
        binding = ActivityForgotBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle window insets for edge-to-edge design
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set click listener for the Continue button
        binding.btnContinue.setOnClickListener {
            // Navigate to VerifyActivity
            val intent = Intent(this, VerifyActivity::class.java)
            startActivity(intent)
        }

        binding.btnCancel.setOnClickListener {
            // Navigate to VerifyActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        setupView()
        playAnimation()
    }

    private fun playAnimation() {

        val title = ObjectAnimator.ofFloat(binding.titleForgot, View.ALPHA, 1f).setDuration(250)
        val logo =
            ObjectAnimator.ofFloat(binding.ivLogoForgot, View.ALPHA, 1f).setDuration(250)
        val desc =
            ObjectAnimator.ofFloat(binding.descForgot, View.ALPHA, 1f).setDuration(250)
        val edEmail =
            ObjectAnimator.ofFloat(binding.emailForgot, View.ALPHA, 1f).setDuration(250)
        val btnContinue =
            ObjectAnimator.ofFloat(binding.btnContinue, View.ALPHA, 1f).setDuration(250)
        val btnCancel =
            ObjectAnimator.ofFloat(binding.btnCancel, View.ALPHA, 1f).setDuration(250)

        AnimatorSet().apply {
            playSequentially(
                logo,
                title,
                desc,
                edEmail,
                btnContinue,
                btnCancel
            )
            startDelay = 250
        }.start()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}
