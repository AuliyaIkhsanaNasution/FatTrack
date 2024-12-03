package com.example.fattrack.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.fattrack.R
import com.example.fattrack.data.ViewModelFactory
import com.example.fattrack.data.viewmodel.HomeViewModel
import com.example.fattrack.data.viewmodel.NotificationViewModel
import com.example.fattrack.data.viewmodel.ProfileViewModel
import com.example.fattrack.databinding.FragmentProfileBinding
import com.example.fattrack.view.login.LoginActivity
import com.example.fattrack.view.profile.EditProfileActivity
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {
    private var _bindingProfile: FragmentProfileBinding? = null
    private val bindingProfile get() = _bindingProfile!!

    private val profileViewModel: ProfileViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private val notificationViewModel: NotificationViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private val userViewModel by viewModels<HomeViewModel> {
        context?.let { ViewModelFactory.getInstance(it) }!!
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Log.d("Permission", "Notifications permission granted")
            } else {
                Log.d("Permission", "Notifications permission rejected")
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        //init binding
        _bindingProfile = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = bindingProfile.root

        clickAllButtons()
        if (Build.VERSION.SDK_INT >= 33) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        observeViewModel()
        userViewModel.getUserById()

        return root
    }

    private fun clickAllButtons() {
        //button edit profile
        bindingProfile.btnEditProfile.setOnClickListener {
            // Intent to move to NotificationsActivity
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            startActivity(intent)
        }

// Button logout
        bindingProfile.logoutButton.setOnClickListener {
            lifecycleScope.launch {
                bindingProfile.progressBar.visibility = View.VISIBLE
                delay(500) // Delay 0,5 detik
                profileViewModel.logout()

                bindingProfile.progressBar.visibility = View.GONE
                navigateToLogin()
            }
        }

    }

    private fun navigateToLogin() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val switchTheme = bindingProfile.themeSwitch

        // Observe LiveData
        profileViewModel.getThemeApp().observe(viewLifecycleOwner) { isDarkModeActive ->
            switchTheme.isChecked = isDarkModeActive
        }

        // Save theme setting on switch change
        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            profileViewModel.saveThemeApp(isChecked)
        }

        // Switch toggle for notifications
        val switchNotification = bindingProfile.switchNotification
        notificationViewModel.notificationToggleState.observe(viewLifecycleOwner) { isEnabled ->
            switchNotification.isChecked = isEnabled
        }

        // save toggle state
        switchNotification.setOnCheckedChangeListener { _, isChecked ->
            notificationViewModel.setNotificationToggle(isChecked)
        }
    }

    private fun observeViewModel() {

        userViewModel.userName.observe(viewLifecycleOwner) { name ->
            bindingProfile.tvNameProfile.text = name?.uppercase() ?: "Default Name"
        }

        userViewModel.userEmail.observe(viewLifecycleOwner) { email ->
            bindingProfile.tvEmailProfile.text = email
        }

        userViewModel.userPhoto.observe(viewLifecycleOwner) { photoUrl ->
            Glide.with(requireContext())
                .load(photoUrl)
                .placeholder(R.drawable.default_pp)
                .into(bindingProfile.ivPhotoProfile)
        }

        userViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            bindingProfile.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        userViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                showToast(it)
            }
        }

    }

    private fun showToast(message: String) {
        val toastCustom = StyleableToast.makeText(requireContext(), message, R.style.StyleableToast)
        toastCustom.show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _bindingProfile = null
    }

}