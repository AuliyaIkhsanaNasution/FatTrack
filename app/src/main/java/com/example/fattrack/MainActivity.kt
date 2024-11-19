package com.example.fattrack

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.fattrack.view.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.github.muddz.styleabletoast.StyleableToast
import android.Manifest
import android.content.pm.PackageManager
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private var backPressedTime: Long = 0
    private lateinit var backToast: StyleableToast
    private val replacedFragmentTags = mutableSetOf<String>()

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        // Set listener for BottomNavigationView
        navView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.navigation_article -> {
                    replaceFragment(ArticleFragment())
                    true
                }
                R.id.navigation_dashboard -> {
                    replaceFragment(DashboardFragment())
                    true
                }
                R.id.navigation_profile -> {
                    replaceFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }

        // Load HomeFragment by default if no fragment is currently loaded
        if (savedInstanceState == null) {
            navView.selectedItemId = R.id.navigation_home


        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            replaceFragment(ScanFragment())
        }
    }


    // Replace fragment with optional back stack control
    private fun replaceFragment(fragment: Fragment, addToBackStack: Boolean = false) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val tag = fragment.javaClass.simpleName // use unique class name
        fragmentTransaction.replace(R.id.nav_host, fragment, tag)
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(tag) // backstack if true
        }

        // Sembunyikan Bottom Navigation untuk fragment tertentu
        val bottomNav = findViewById<BottomNavigationView>(R.id.nav_view)
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        if (fragment is ScanFragment) {
            bottomNav.visibility = View.GONE // Sembunyikan Bottom Navigation
            fab.visibility = View.GONE
        } else {
            bottomNav.visibility = View.VISIBLE // Tampilkan kembali
            fab.visibility = View.VISIBLE
        }

        replacedFragmentTags.add(tag) // save tag
        fragmentTransaction.commit()
    }


    //handle back pressed
    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.nav_host)
        val currentTag = currentFragment?.tag

        if (currentTag != null && replacedFragmentTags.contains(currentTag)) {
            // handle from func replaceFragment
            if (System.currentTimeMillis() - backPressedTime < 2000) {
                backToast.cancel()
                replacedFragmentTags.remove(currentTag) // remove tag fragment
                super.onBackPressed() // next proses back
            } else {
                // show toast if back pressed
                backToast = StyleableToast.makeText(applicationContext, "Klik sekali lagi untuk keluar", R.style.StyleableToast)
                backToast.show()
                backPressedTime = System.currentTimeMillis() // Update back time
            }
        } else {
            // default back
            super.onBackPressed()
        }
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}
