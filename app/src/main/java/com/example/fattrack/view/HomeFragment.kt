package com.example.fattrack.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.fattrack.R
import com.example.fattrack.data.ViewModelFactory
import com.example.fattrack.data.adapter.ArticleAdapter
import com.example.fattrack.data.viewmodel.ArticlesViewModel
import com.example.fattrack.data.viewmodel.HomeViewModel
import com.example.fattrack.data.viewmodel.MainViewModel
import com.example.fattrack.databinding.FragmentHomeBinding
import com.example.fattrack.view.notifications.NotificationsActivity
import io.github.muddz.styleabletoast.StyleableToast

class HomeFragment : Fragment() {

    // Declare the ViewBinding variable
    private lateinit var viewModelArticle: ArticlesViewModel
    private lateinit var adapter: ArticleAdapter
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<MainViewModel> {
        context?.let { ViewModelFactory.getInstance(it) }!!
    }
    private val homeViewModel by viewModels<HomeViewModel> {
        context?.let { ViewModelFactory.getInstance(it) }!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment using ViewBinding
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Initialize ViewModel with ViewModelFactory
        val factory = ViewModelFactory.getInstance(this.requireContext())
        viewModelArticle = ViewModelProvider(this, factory)[ArticlesViewModel::class.java]

        setupRecyclerView()
        setupObserver()
        observeViewModel()

        // Panggil fungsi untuk mengambil data user
        homeViewModel.getUserById()

        // Fetch data from ViewModel
        viewModelArticle.fetchArticles()
        homeViewModel.fetchHomeData()

        // Return the root view of the fragment
        return binding.root
    }

    private fun setupRecyclerView() {
        // Initialize the RecyclerView and adapter
        adapter = ArticleAdapter()
        binding.recyclerHome.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@HomeFragment.adapter
        }
    }

    private fun setupObserver() {
        // Observe articles LiveData
        viewModelArticle.articles.observe(viewLifecycleOwner) { articles ->
            val limitedArticles = articles.take(5)
            adapter.setDataArticles(limitedArticles)
        }

        // Observe loading state LiveData
        viewModelArticle.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Observe error messages LiveData
        viewModelArticle.errorMessage.observe(viewLifecycleOwner) { message ->
            if (!message.isNullOrEmpty()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNotifications.setOnClickListener {
            // Intent to move to NotificationsActivity
            val intent = Intent(requireContext(), NotificationsActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clean up the binding reference to avoid memory leaks
    }

    @SuppressLint("SetTextI18n")
    private fun observeViewModel() {
        homeViewModel.totalProtein.observe(viewLifecycleOwner) { totalProtein ->
            binding.totalProtein.text = totalProtein?.toString() ?: "0.0 g"
        }

        homeViewModel.totalKarbohidrat.observe(viewLifecycleOwner) { totalKarbohidrat ->
            binding.totalKarbohidrat.text = totalKarbohidrat?.toString() ?: "0.0 g"
        }

        homeViewModel.totalLemak.observe(viewLifecycleOwner) { totalLemak ->
            binding.totalLemak.text = totalLemak?.toString() ?: "0.0 g"
        }

        homeViewModel.date.observe(viewLifecycleOwner) { formattedDate ->
            binding.textDate.text = formattedDate ?: "Tanggal tidak tersedia"
        }

        homeViewModel.userName.observe(viewLifecycleOwner) { name ->
             binding.userName.text = "Halo, ${name ?: "Unknown Name"}"
        }

        homeViewModel.userPhoto.observe(viewLifecycleOwner) { photoUrl ->
            Glide.with(requireContext())
                .load(photoUrl)
                .placeholder(R.drawable.default_pp)
                .into(binding.ivProfile)
        }

        homeViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        homeViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                showToast(it)
            }
        }

        homeViewModel.totalKalori.observe(viewLifecycleOwner) { totalKalori ->
            binding.totalKalori.text = "${totalKalori ?: 0}"
        }

    }



    private fun showToast(message: String) {
        val toastCustom = StyleableToast.makeText(requireContext(), message, R.style.StyleableToast)
        toastCustom.show()
    }

}
