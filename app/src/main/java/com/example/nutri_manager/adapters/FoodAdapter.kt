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
            textView11.text = food.description
            textView12.text = food.brandOwner
            textView13.text = food.ingredients
            textView14.text = food.fdcId.toString()

//
//            textView11.text = "description: ${food.description}"
//            textView12.text = "brandOwner: ${food.brandOwner}"
//            textView13.text = "ingredients: ${food.ingredients}"
//            textView14.text = "fdcId: ${food.fdcId.toString()}"
//            textView15.text = "dataType: ${food.dataType}"
//            textView16.text = "gtinUpc: ${food.gtinUpc}"
//            textView17.text = "publishedDate: ${food.publishedDate}"
//            textView18.text = "score: ${food.score.toString()}"
//            textView19.text = "allHighlightFields: ${food.allHighlightFields}"
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