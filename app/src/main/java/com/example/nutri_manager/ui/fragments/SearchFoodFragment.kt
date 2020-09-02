package com.example.nutri_manager.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.nutri_manager.R
import com.example.nutri_manager.ui.FoodActivity
import com.example.nutri_manager.util.Constants
import com.example.nutri_manager.util.Constants.Companion.SEARCH_NEWS_TIME_DELAY
import com.example.nutri_manager.util.Resource
import com.example.nutrimanager.ui.FoodViewModel
import kotlinx.android.synthetic.main.fragment_search_food.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFoodFragment : Fragment(R.layout.fragment_search_food) {

    lateinit var foodViewModel: FoodViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        foodViewModel = (activity as FoodActivity).viewModel
//        searchFoodButtonTemp.setOnClickListener(View.OnClickListener {
//            foodViewModel.searchFoods(etSearchTemp.text.toString())
//
//        })

        var job: Job? = null
        etSearchTemp.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_NEWS_TIME_DELAY)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
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

//                        newsAdapter.differ.submitList(foodsResponse.foods.toList())
//                        val totalPages = foodsResponse.totalHits / Constants.QUERY_PAGE_SIZE + 2
//                        isLastPage = viewModel.searchFoodsPage == totalPages
//                        if(isLastPage) {
//                            rvSearchFoods.setPadding(0, 0, 0, 0)
//                        }
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

}