package com.example.fattrack.view.forgotpassword

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.os.Build
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fattrack.R
import com.example.fattrack.databinding.ActivityResetPasswordBinding
import com.example.fattrack.view.login.LoginActivity

class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResetPasswordBinding // Declare binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize View Binding
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        // Set click listener for the Continue button
//        binding.btnSavePassword.setOnClickListener {
//            // Navigate to VerifyActivity
//            val intent = Intent(this, VerifyActivity::class.java)
//            startActivity(intent)
//        }

        binding.btnCancelPassword?.setOnClickListener {
            // Navigate to VerifyActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        setupView()
        playAnimation()
    }

    private fun playAnimation() {

        val title = ObjectAnimator.ofFloat(binding.titleResetPassword, View.ALPHA, 1f).setDuration(300)
        val logo =
            ObjectAnimator.ofFloat(binding.ivResetPassword, View.ALPHA, 1f).setDuration(300)
        val descReset =
            ObjectAnimator.ofFloat(binding.descResetPassword, View.ALPHA, 1f).setDuration(300)
        val edOldPassword =
            ObjectAnimator.ofFloat(binding.oldPassword, View.ALPHA, 1f).setDuration(300)
        val newPassword =
            ObjectAnimator.ofFloat(binding.newPassword, View.ALPHA, 1f).setDuration(300)
        val saveButton =
            ObjectAnimator.ofFloat(binding.btnSavePassword, View.ALPHA, 1f).setDuration(300)
        val cancelButton =
            ObjectAnimator.ofFloat(binding.btnCancelPassword, View.ALPHA, 1f).setDuration(300)

        AnimatorSet().apply {
            playSequentially(
                title,
                logo,
                descReset,
                edOldPassword,
                newPassword,
                saveButton,
                cancelButton
            )
            startDelay = 200
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