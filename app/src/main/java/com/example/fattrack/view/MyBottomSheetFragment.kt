package com.example.fattrack.view

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.fattrack.R
import com.example.fattrack.data.data.NutritionData
import com.example.fattrack.databinding.BottomSheetBinding
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

@Suppress("DEPRECATION")
class MyBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: BottomSheetBinding? = null
    private val binding get() = _binding!!
    private var nutritionalInfo: NutritionData? = null


    companion object {
        private const val ARG_NUTRITIONAL_INFO = "arg_nutritional_info"

        fun newInstance(nutritionalInfo: NutritionData): MyBottomSheetFragment {
            val fragment = MyBottomSheetFragment()
            val args = Bundle()
            args.putParcelable(ARG_NUTRITIONAL_INFO, nutritionalInfo)
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nutritionalInfo = arguments?.getParcelable(ARG_NUTRITIONAL_INFO)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // init binding
        _binding = BottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // set style BottomSheetDialog
        return BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //setup
        viewSetup()
        chartSetup()
    }

    private fun viewSetup() {
        // Update UI
        nutritionalInfo?.let { info ->
            binding.titleSheet.text= info.nama
            binding.descSheet.text = info.deskripsi
            binding.tvKalori.text = "${info.kalori} kkal"
            binding.tvKarbohidrat.text = "${info.karbohidrat} g"
            binding.tvLemak.text = "${info.lemak} g"
            binding.tvProtein.text = "${info.protein} g"
        }
    }


    // setup for chart
    private fun chartSetup() {
        var pieEntries: List<PieEntry> = emptyList()
        // Dummy data for PieChart
        nutritionalInfo?.let { info ->
            pieEntries = listOf(
                PieEntry(info.kalori?.toFloat() ?: 0f, "Kalori"),
                PieEntry(info.protein?.toFloat() ?: 0f, "Protein"),
                PieEntry(info.lemak?.toFloat() ?: 0f, "Lemak"),
                PieEntry(info.karbohidrat?.toFloat() ?: 0f, "Karbohidrat")
            )
        }

        //colors
        val colorChart = listOf(
            ContextCompat.getColor(requireContext(), R.color.Error),
            ContextCompat.getColor(requireContext(), R.color.Primary),
            ContextCompat.getColor(requireContext(), R.color.TersierVariant),
            ContextCompat.getColor(requireContext(), R.color.Tersier)
        )

        // create PieDataSet
        val pieDataSet = PieDataSet(pieEntries, "").apply {
//            colors = ColorTemplate.MATERIAL_COLORS.toList()
            colors = colorChart
            valueTextColor = ContextCompat.getColor(requireContext(), R.color.chart_text_color)
            valueTextSize = 12f
        }

        // create PieData
        val pieData = PieData(pieDataSet)

        // ref to PieChart
        val pieChart: PieChart = binding.chartSheet
        pieChart.data = pieData

        // custom PieChart
        pieChart.isDrawHoleEnabled = true // hole
        pieChart.holeRadius = 40f // hole Radius
        pieChart.setEntryLabelColor(ContextCompat.getColor(requireContext(), R.color.chart_text_color)) // color entry label
        pieChart.setEntryLabelTextSize(12f) // size label
        pieChart.description.isEnabled = false
        pieChart.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        pieChart.legend.textColor = ContextCompat.getColor(requireContext(), R.color.chart_text_color)

        // Refresh chart
        pieChart.invalidate()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
