package com.example.nutri_manager.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.nutri_manager.R
import com.example.nutri_manager.models.FoodConsumption
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firestore.v1.DocumentTransform
import com.google.type.Date
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.sql.Types.TIMESTAMP


class HomeFragment : Fragment(R.layout.fragment_home) {
    val collectionRef = FirebaseAuth.getInstance().uid!!.let {
        FirebaseFirestore.getInstance().collection(
            it
        )
    }
    val foodConsumptionInstanceList: ArrayList<FoodConsumption> = ArrayList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchFoodButton.setOnClickListener(View.OnClickListener {
            findNavController().navigate(
                R.id.action_homeFragment_to_searchFoodFragment2
            )
        })
//        retriveRealTimeFoodConsuptionData()
    }


//    private fun retriveRealTimeFoodConsuptionData() = CoroutineScope(Dispatchers.IO).launch {
//        val querySnapshot = collectionRef.get().await()
//        withContext(Dispatchers.Main) {
//            Toast.makeText(
//                requireContext(), querySnapshot.documents.size, Toast.LENGTH_SHORT
//            ).show()
//        }
//        for (document in querySnapshot.documents) {
////                val foodConsumption = document.toObject<FoodConsumption>()!!
////                foodConsumptionInstanceList.add(foodConsumption)
//        }
//    }
}