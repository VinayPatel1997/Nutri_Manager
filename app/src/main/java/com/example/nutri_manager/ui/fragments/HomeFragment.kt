package com.example.nutri_manager.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.nutri_manager.R
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(R.layout.fragment_home) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchFoodButton.setOnClickListener(View.OnClickListener {
            findNavController().navigate(
                R.id.action_homeFragment_to_searchFoodFragment2
            )
        })
    }
}