package com.example.nutri_manager.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.nutri_manager.R
import com.example.nutri_manager.ui.FoodActivity
import com.example.nutri_manager.ui.FoodViewModel
import com.google.android.gms.maps.GoogleMap
//    val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());

class MapFragment : Fragment(R.layout.fragment_map) {
    private lateinit var mMap: GoogleMap
    lateinit var foodViewModel: FoodViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        foodViewModel = (activity as FoodActivity).viewModel

//        val mapFragment = supportFragmentManger

    }

}