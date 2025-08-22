package com.example.skyway.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.skyway.R
import com.example.skyway.model.BestOffer

class BestOffersAdapter(
	private val items: List<BestOffer>
) : RecyclerView.Adapter<BestOffersAdapter.BestOfferViewHolder>() {

	inner class BestOfferViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		private val image: ImageView = itemView.findViewById(R.id.offerImage)
		private val country: TextView = itemView.findViewById(R.id.offerCountry)
		private val city: TextView = itemView.findViewById(R.id.offerCity)
		private val price: TextView = itemView.findViewById(R.id.offerPrice)

		fun bind(item: BestOffer) {
			image.setImageResource(item.imageRes)
			country.text = item.country
			city.text = item.city
			price.text = item.price
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestOfferViewHolder {
		val view = LayoutInflater.from(parent.context).inflate(R.layout.item_best_offer, parent, false)
		return BestOfferViewHolder(view)
	}

	override fun onBindViewHolder(holder: BestOfferViewHolder, position: Int) = holder.bind(items[position])

	override fun getItemCount(): Int = items.size
}
