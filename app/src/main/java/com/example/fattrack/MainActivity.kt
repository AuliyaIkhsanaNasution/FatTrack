package com.example.fattrack

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.fattrack.view.HomeFragment
import com.example.fattrack.view.ArticleFragment
import com.example.fattrack.view.DashboardFragment
import com.example.fattrack.view.ProfileFragment
import com.example.fattrack.view.ScanFragment // Assuming you have a ScanFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Initialize BottomNavigationView
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        // Set the listener for item selection in BottomNavigationView
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

        // If no fragment is loaded initially, load the home fragment
        if (savedInstanceState == null) {
            navView.selectedItemId = R.id.navigation_home // Set default selected item
        }

        // Apply window insets to ensure the system bars are not overlapping
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize the FloatingActionButton (FAB)
        val fab: FloatingActionButton = findViewById(R.id.fab)

        // Set click listener for the FAB
        fab.setOnClickListener {
            // Replace the current fragment with ScanFragment when the FAB is clicked
            replaceFragment(ScanFragment())
        }
    }

    // Helper function to replace the fragment
    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.navhost, fragment)  // navhost is the container in your layout
        fragmentTransaction.addToBackStack(null) // Add to back stack if you want to navigate back
        fragmentTransaction.commit()
    }
}
