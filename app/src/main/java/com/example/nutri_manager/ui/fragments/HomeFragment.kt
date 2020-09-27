package com.example.nutri_manager.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.nutri_manager.R
import com.example.nutri_manager.models.Food
import com.example.nutri_manager.models.FoodConsumption
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.android.synthetic.main.fragment_home.*
import java.lang.Exception

class HomeFragment : Fragment(R.layout.fragment_home) {
    val collectionRef = FirebaseAuth.getInstance().uid?.let {
        FirebaseFirestore.getInstance().collection(
            it
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchFoodButton.setOnClickListener(View.OnClickListener {
            findNavController().navigate(
                R.id.action_homeFragment_to_searchFoodFragment2
            )
        })
//        retriveFoodConsuptionData()
    }

    private fun retriveFoodConsuptionData() {
        collectionRef?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            firebaseFirestoreException?.let {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }
            querySnapshot?.let {
                for (date in it) {
                    val foodConsumption = date.toObject<FoodConsumption>()
                }
            }
        }
    }
}