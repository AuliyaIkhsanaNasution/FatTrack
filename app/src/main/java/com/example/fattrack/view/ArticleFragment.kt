package com.example.fattrack.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fattrack.R
import com.example.fattrack.data.adapter.ArticleAdapter
import com.example.fattrack.data.ArticleData

class ArticleFragment : Fragment() {

    private lateinit var rvArticle: RecyclerView
    private val list = ArrayList<ArticleData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_article, container, false)

        // Inisialisasi RecyclerView
        rvArticle = view.findViewById(R.id.rv_article)
        rvArticle.setHasFixedSize(true)

        // Tambahkan data ke daftar
        list.addAll(getListArticle())
        showRecyclerList()

        return view
    }

    private fun getListArticle(): ArrayList<ArticleData> {
        val dataTitle = resources.getStringArray(R.array.data_title)
        val dataName = resources.getStringArray(R.array.data_author)
        val dataDate = resources.getStringArray(R.array.data_date)
        val dataDescription = resources.getStringArray(R.array.data_description)
        val dataPhoto = resources.getStringArray(R.array.data_photo)
        val listArticle = ArrayList<ArticleData>()

        for (i in dataName.indices) {
            val article = ArticleData(
                title = dataTitle[i],
                name = dataName[i],
                date = dataDate[i],
                description = dataDescription[i],
                photo = dataPhoto[i]
            )
            listArticle.add(article)
        }
        return listArticle
    }

    private fun showRecyclerList() {
        rvArticle.layoutManager = LinearLayoutManager(requireContext()) // Gunakan requireContext()
        val articleAdapter = ArticleAdapter(list) // Pastikan adapter benar
        rvArticle.adapter = articleAdapter
    }
}
