package com.example.nutri_manager.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.nutri_manager.R
import com.example.nutri_manager.models.FoodNutrient
import kotlinx.android.synthetic.main.item_nutrient_preview.view.*

class NutrientAdapter : RecyclerView.Adapter<NutrientAdapter.NutrientViewHolder>(){
    inner class NutrientViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView)

    private val differCallBack = object : DiffUtil.ItemCallback<FoodNutrient>() {
        override fun areItemsTheSame(oldItem: FoodNutrient, newItem: FoodNutrient): Boolean {
            return oldItem.nutrientId == newItem.nutrientId
        }

        override fun areContentsTheSame(oldItem: FoodNutrient, newItem: FoodNutrient): Boolean {
            return oldItem.value == newItem.value
        }
    }
    val differ = AsyncListDiffer(this, differCallBack)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NutrientViewHolder {
        return NutrientViewHolder(
            LayoutInflater.from(parent.context)
            .inflate(R.layout.item_nutrient_preview, parent, false)
        )
    }
    override fun getItemCount(): Int {
        return differ.currentList.size
    }
    override fun onBindViewHolder(holder: NutrientViewHolder, position: Int) {
        val nutrient  = differ.currentList[position]
        holder.itemView.apply {
            nutrient_name.text = nutrient.nutrientName
            nutrient_value.text = nutrient.value.toString()
            nutrient_unit.text = nutrient.unitName

        }
    }


}