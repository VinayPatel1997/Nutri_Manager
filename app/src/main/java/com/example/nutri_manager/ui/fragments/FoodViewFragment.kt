package com.example.nutri_manager.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.nutri_manager.R
import com.example.nutrimanager.ui.FoodViewModel
import kotlinx.android.synthetic.main.fragment_food_view.*

class FoodViewFragment : Fragment(R.layout.fragment_food_view) {

    lateinit var viewModel: FoodViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textView.setText("Food View Fragment")

    }
}