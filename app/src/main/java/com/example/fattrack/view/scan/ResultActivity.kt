package com.example.fattrack.view.scan

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fattrack.databinding.ActivityResultBinding
import com.example.fattrack.view.MyBottomSheetFragment

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate layout menggunakan View Binding
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil URI dari Intent
        val imageUri = intent.getStringExtra("image_uri")?.let { Uri.parse(it) }

        if (imageUri != null) {
            // Set URI ke ImageView menggunakan View Binding
            binding.previewImageView.setImageURI(imageUri)
        } else {
            // Jika URI tidak ada, tampilkan pesan kesalahan
            Toast.makeText(this, "Gambar tidak ditemukan.", Toast.LENGTH_SHORT).show()
        }

        // Cek apakah bottom sheet harus ditampilkan
        val showBottomSheet = intent.getBooleanExtra("SHOW_BOTTOM_SHEET", false)
        if (showBottomSheet) {
            displayBottomSheet()
        }
    }

    // Fungsi untuk menampilkan Bottom Sheet
    private fun displayBottomSheet(){
        val bottomSheetFragment = MyBottomSheetFragment()
        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
    }
}
