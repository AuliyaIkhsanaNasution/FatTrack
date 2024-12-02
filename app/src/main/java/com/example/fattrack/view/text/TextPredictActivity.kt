package com.example.fattrack.view.text

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.fattrack.R
import com.example.fattrack.data.ViewModelFactory
import com.example.fattrack.data.data.NutritionData
import com.example.fattrack.data.services.responses.Data
import com.example.fattrack.data.viewmodel.PredictViewModel
import com.example.fattrack.databinding.ActivityTextPredictBinding
import com.example.fattrack.view.MyBottomSheetFragment
import io.github.muddz.styleabletoast.StyleableToast

class TextPredictActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTextPredictBinding
    private val predictViewModel: PredictViewModel by viewModels {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTextPredictBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeViewModel()
        setupUI()

        binding.back.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun observeViewModel() {
        predictViewModel.searchResponse.observe(this) { response ->
            response?.let { searchResponse ->
                val foodData = searchResponse.responseSearchFood?.firstOrNull()?.data?.let { data ->
                    mapToParcelable(data)
                }

                foodData?.let { nutritionData ->
                    displayBottomSheet(nutritionData)
                } ?: showToast("No matching food found.")
            } ?: showToast("Response is null.")
        }

        predictViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnPredict.isEnabled = !isLoading
        }

        predictViewModel.errorMessage.observe(this) { errorMessage ->
            errorMessage?.let { showErrorDialog(it) }
        }
    }

    private fun setupUI() {
        binding.btnPredict.setOnClickListener {
            val query = binding.edTextPredict.text.toString().trim()
            if (query.isNotEmpty()) {
                predictViewModel.searchFood(query)
            } else {
                showToast("Please enter a food name.")
            }
        }
    }

    private fun displayBottomSheet(foodData: NutritionData) {
        val bottomSheetFragment = MyBottomSheetFragment.newInstance(foodData)
        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
    }

    private fun mapToParcelable(response: Data?): NutritionData {
        return NutritionData(
            deskripsi = response?.deskripsi,
            kalori = response?.kalori,
            karbohidrat = response?.karbohidrat.toString().toDoubleOrNull() ?: 0.0,
            lemak = response?.lemak.toString().toDoubleOrNull() ?: 0.0,
            nama = response?.nama,
            protein = response?.protein.toString().toDoubleOrNull() ?: 0.0
        )
    }

    private fun showToast(message: String) {
        val toastCustom = StyleableToast.makeText(applicationContext, message, R.style.StyleableToast)
        toastCustom.show()
    }

    private fun showErrorDialog(message: String?) {
        SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
            .setTitleText("Failed!")
            .setContentText("$message. Please try again.")
            .setConfirmText("OK")
            .show()
    }
}
