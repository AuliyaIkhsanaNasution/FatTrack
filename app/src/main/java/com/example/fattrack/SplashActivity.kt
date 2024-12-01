package com.example.fattrack

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.fattrack.data.ViewModelFactory
import com.example.fattrack.data.pref.AuthPreferences
import com.example.fattrack.data.pref.authSession
import com.example.fattrack.data.viewmodel.MainViewModel
import com.example.fattrack.databinding.ActivitySplashBinding
import com.example.fattrack.view.login.LoginActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var authPreferences: AuthPreferences
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize AuthPreferences
        authPreferences = AuthPreferences.getInstance(this.authSession)

        Handler(Looper.getMainLooper()).postDelayed({
            if (isInternetAvailable()) {
                observeSession()
            } else {
                showErrorDialog("Check your connection.")
            }
        }, 1000)
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

    //alert dialog error
    @Suppress("SameParameterValue")
    private fun showErrorDialog(message: String?) {
        SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
            .setTitleText("Failed!")
            .setContentText("$message")
            .setConfirmText("OK")
            .setConfirmClickListener { dialog ->
                dialog.dismissWithAnimation()
            }
            .show()
    }

    private fun observeSession() {
        viewModel.getSession().observe(this) { user ->
            if (user.isLogin && user.idUser.isNotEmpty()) {
                navigateToMainActivity()
            } else {
                navigateToLoginActivity()
            }
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}