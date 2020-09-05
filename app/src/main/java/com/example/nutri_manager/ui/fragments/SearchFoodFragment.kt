package com.example.nutri_manager.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nutri_manager.R
import com.example.nutri_manager.adapters.FoodAdapter
import com.example.nutri_manager.ui.FoodActivity
import com.example.nutri_manager.util.Constants
import com.example.nutri_manager.util.Constants.Companion.SEARCH_FOOD_TIME_DELAY
import com.example.nutri_manager.util.Resource
import com.example.nutri_manager.ui.FoodViewModel
import kotlinx.android.synthetic.main.fragment_search_food.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFoodFragment : Fragment(R.layout.fragment_search_food) {

    lateinit var foodViewModel: FoodViewModel
    lateinit var foodAdapter : FoodAdapter
    val TAG = "Search Food Fragment"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        foodViewModel = (activity as FoodActivity).viewModel
        setupRecyclerView()

        foodAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("food", it)
            }
            findNavController().navigate(
                R.id.action_searchFoodFragment2_to_foodViewFragment,
                bundle
            )
        }

        var job: Job? = null
        etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_FOOD_TIME_DELAY)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        foodViewModel.searchFoodsPage = 1
                        foodViewModel.searchFoodsResponse = null
                        foodViewModel.searchFoods(editable.toString())
                    }
                }
            }
        }

        foodViewModel.searchFoods.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { foodsResponse ->
                        Toast.makeText(context, "Successfull responce aavi gyo", Toast.LENGTH_LONG).show()
                        foodAdapter.differ.submitList(foodsResponse.foods.toList())
                        val totalPages = foodsResponse.totalHits / Constants.QUERY_PAGE_SIZE + 2
                        isLastPage = foodViewModel.searchFoodsPage == totalPages
                        if(isLastPage) {
                            rvSearchFoods.setPadding(0, 0, 0, 0)
                        }
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(activity, "An error occured: $message", Toast.LENGTH_LONG)
                            .show()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })

    }

    private fun hideProgressBar() {
        paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling
            if(shouldPaginate) {
                foodViewModel.searchFoods(etSearch.text.toString())
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }


    private fun setupRecyclerView() {
        foodAdapter = FoodAdapter()
        rvSearchFoods.apply {
            adapter = foodAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@SearchFoodFragment.scrollListener)
        }
    }

}