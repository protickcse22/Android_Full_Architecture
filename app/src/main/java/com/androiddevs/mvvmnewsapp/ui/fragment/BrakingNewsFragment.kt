package com.androiddevs.mvvmnewsapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.adapters.NewsAdapter
import com.androiddevs.mvvmnewsapp.databinding.FragmentBreakingNewsBinding
import com.androiddevs.mvvmnewsapp.utils.hideProgressBar
import com.androiddevs.mvvmnewsapp.utils.showProgressBar
import com.androiddevs.mvvmnewsapp.viewmodel.NewsViewModel
import com.androiddevs.mvvmnewsapp.utils.Resource
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class BrakingNewsFragment : Fragment(R.layout.fragment_breaking_news),
    NewsAdapter.OnArticleClickListener {
    private val TAG = "BrakingNewsFragment"
    private val newsViewModel: NewsViewModel by viewModels()
    private var _binding: FragmentBreakingNewsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: NewsAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentBreakingNewsBinding.bind(view)
        setUpRecyclerView()
        newsViewModel.breakingNews.observe(viewLifecycleOwner, Observer { response ->

            when (response) {
                is Resource.Success -> {
                    binding.paginationProgressBar.hideProgressBar()
                    response.data?.let { newsResponse ->
                        adapter.differ.submitList(newsResponse.articles)
                    }
                }
                is Resource.Error -> {
                    binding.paginationProgressBar.hideProgressBar()
                    response.message?.let { message ->
                        Log.d(TAG, "An error occured: $message")
                    }

                }
                is Resource.Loading -> {
                    binding.paginationProgressBar.showProgressBar()
                }
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpRecyclerView() {
        adapter = NewsAdapter(this)
        binding.apply {
            rvBreakingNews.setHasFixedSize(true)
            rvBreakingNews.adapter = adapter
            rvBreakingNews.layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onItemClick(position: Int) {
        val bundle = Bundle().apply {
            putSerializable("article", adapter.differ.currentList[position])
        }
        findNavController().navigate(
            R.id.action_brakingNewsFragment_to_articleFragment,
            bundle
        )
    }


}