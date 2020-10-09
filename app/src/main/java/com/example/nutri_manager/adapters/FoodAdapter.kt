package com.example.nutri_manager.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.nutri_manager.R
import com.example.nutri_manager.models.Food
import kotlinx.android.synthetic.main.item_food_preview.view.*

class FoodAdapter : RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {
    inner class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallBack = object : DiffUtil.ItemCallback<Food>() {
        override fun areItemsTheSame(oldItem: Food, newItem: Food): Boolean {
            return oldItem.fdcId == newItem.fdcId
        }

        override fun areContentsTheSame(oldItem: Food, newItem: Food): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallBack)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        return FoodViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_food_preview, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val food = differ.currentList[position]
        holder.itemView.apply {
            description.text = food.description
            ingredients.text = food.ingredients
            brandOwner.text = food.brandOwner
            setOnClickListener {
                onItemClickListener?.let { it(food) }
            }
        }
    }

    private var onItemClickListener: ((Food) -> Unit)? = null
    fun setOnItemClickListener(listener: (Food) -> Unit) {
        onItemClickListener = listener
    }
}