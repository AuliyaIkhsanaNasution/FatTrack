package com.example.fattrack.view.scan

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.fattrack.MainActivity
import com.example.fattrack.R
import com.example.fattrack.data.ViewModelFactory
import com.example.fattrack.data.model.NutritionData
import com.example.fattrack.data.services.responses.NutritionalInfo
import com.example.fattrack.data.viewmodel.PredictViewModel
import com.example.fattrack.databinding.ActivityResultBinding
import com.example.fattrack.view.MyBottomSheetFragment
import io.github.muddz.styleabletoast.StyleableToast
import java.io.File

class ResultActivity : AppCompatActivity() {
    private val predictViewModel: PredictViewModel by viewModels {
        ViewModelFactory.getInstance(application)
    }
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // init binding
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // get URI from Intent
        val imageUri = intent.getStringExtra("image_uri")?.let { Uri.parse(it) }

        //set URI
        if (imageUri != null) {
            binding.previewImageView.setImageURI(imageUri)
        } else {
            showToast("image not found.")
        }

        //buttons click & send file
        val imageFile = imageUri?.let { uriToFile(it, this).reduceFileImage() }
        if (imageFile != null) {
            buttonsClick(imageFile)
        }

        //observe
        observeViewModel()
    }

    //buttons click setup
    private fun buttonsClick(imageUri: File) {
        binding.btnScan.setOnClickListener {
            predictViewModel.predictImage(imageUri)
        }
        binding.btnCancel.setOnClickListener {
            finishActivity()
        }
        binding.btnHome.setOnClickListener {
            //finish and open home
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.btnBack.setOnClickListener {
            finishActivity()
        }
    }


    private fun observeViewModel() {
        //observe predict
        predictViewModel.predictResponse.observe(this) { response ->
            response?.let {
                if (it.code == 200) {
                    //open bottom and send data
                    val nutrition = mapToParcelable(it.data?.nutritionalInfo)
                    displayBottomSheet(nutrition)
                } else {
                    it.data?.message?.let { it1 -> showToast(it1) }
                }
            }
        }

        //observe loading
        predictViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
        }

        //observe error
        predictViewModel.errorMessage.observe(this) { errorMessage ->
            if (errorMessage != null) {
                showErrorDialog(errorMessage)
            }
        }
    }


    // display bottom sheet
    private fun displayBottomSheet(nutritionalInfo: NutritionData) {
        //send data and open bottom sheet
        val bottomSheetFragment = MyBottomSheetFragment.newInstance(nutritionalInfo)
        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
    }


    //toast custom
    private fun showToast(message: String) {
        val toastCustom = StyleableToast.makeText(applicationContext, message, R.style.StyleableToast)
        toastCustom.show()
    }


    //alert dialog error
    private fun showErrorDialog(message: String?) {
        SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
            .setTitleText("Failed!")
            .setContentText("Error : $message")
            .setConfirmText("OK")
            .setConfirmClickListener { dialog ->
                dialog.dismissWithAnimation()
            }
            .show()
    }


    private fun finishActivity() {
        finish()
    }


    //map to parcelable
    private fun mapToParcelable(response: NutritionalInfo?): NutritionData {
        return NutritionData(
            deskripsi = response?.deskripsi,
            kalori = response?.kalori,
            karbohidrat = response?.karbohidrat.toString().toDouble(),
            lemak = response?.lemak.toString().toDouble(),
            nama = response?.nama,
            protein = response?.protein.toString().toDouble()
        )
    }


}
