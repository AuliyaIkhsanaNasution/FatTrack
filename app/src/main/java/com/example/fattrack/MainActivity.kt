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
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.fattrack.view.scan.CameraActivity

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

        //run fun
        setupBottomNavigation()
        setupInsets()
        setupFab()

        // Load HomeFragment by default
        if (savedInstanceState == null) {
            findViewById<BottomNavigationView>(R.id.nav_view).selectedItemId = R.id.navigation_home
        }
    }


    // BottomNavigationView
    private fun setupBottomNavigation() {
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> replaceFragment(HomeFragment())
                R.id.navigation_article -> replaceFragment(ArticleFragment())
                R.id.navigation_dashboard -> replaceFragment(DashboardFragment())
                R.id.navigation_profile -> replaceFragment(ProfileFragment())
                else -> false
            }
        }
    }


    // Inset bottom navigation
    private fun setupInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
    }


    // setup floatingActionButton
    private fun setupFab() {
        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            startActivity(Intent(this, CameraActivity::class.java))
        }
    }


    // Replace fragment with optional back stack control
    private fun replaceFragment(fragment: Fragment, addToBackStack: Boolean = false): Boolean {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val tag = fragment.javaClass.simpleName // use unique class name
        fragmentTransaction.replace(R.id.nav_host, fragment, tag)
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(tag) // backstack if true
        }

        replacedFragmentTags.add(tag) // save tag
        fragmentTransaction.commit()
        return true
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
