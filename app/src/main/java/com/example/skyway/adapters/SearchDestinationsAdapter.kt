package com.example.skyway.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.skyway.R
import com.example.skyway.model.SearchDestination

class SearchDestinationsAdapter(
    private val items: List<SearchDestination>
) : RecyclerView.Adapter<SearchDestinationsAdapter.DestViewHolder>() {

    inner class DestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image: ImageView = itemView.findViewById(R.id.destImage)
        private val country: TextView = itemView.findViewById(R.id.destCountry)
        private val city: TextView = itemView.findViewById(R.id.destCity)
        private val rating: TextView = itemView.findViewById(R.id.destRating)
        private val favorite: ImageView = itemView.findViewById(R.id.destFavorite)

        fun bind(item: SearchDestination) {
            image.setImageResource(item.imageRes)
            country.text = item.country
            city.text = item.city
            rating.text = item.rating
            updateFavorite(item)
            favorite.setOnClickListener {
                item.isFavorite = !item.isFavorite
                updateFavorite(item)
            }
        }

        private fun updateFavorite(item: SearchDestination) {
            favorite.setImageResource(
                if (item.isFavorite) R.drawable.ic_favorite_selected else R.drawable.ic_favorite
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DestViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_search_destination, parent, false)
        return DestViewHolder(v)
    }

    override fun onBindViewHolder(holder: DestViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount(): Int = items.size
}
