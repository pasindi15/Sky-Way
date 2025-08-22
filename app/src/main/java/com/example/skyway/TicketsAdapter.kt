package com.example.skyway

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.skyway.model.Ticket

class TicketsAdapter(private var tickets: List<Ticket>) : RecyclerView.Adapter<TicketsAdapter.TicketVH>() {

    inner class TicketVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val depTime: TextView = itemView.findViewById(R.id.departureTime)
        val depCode: TextView = itemView.findViewById(R.id.departureCode)
        val depCity: TextView = itemView.findViewById(R.id.departureCity)
        val depAirport: TextView = itemView.findViewById(R.id.departureAirport)
        val arrTime: TextView = itemView.findViewById(R.id.arrivalTime)
        val arrCode: TextView = itemView.findViewById(R.id.arrivalCode)
        val arrCity: TextView = itemView.findViewById(R.id.arrivalCity)
        val arrAirport: TextView = itemView.findViewById(R.id.arrivalAirport)
        val airline: TextView = itemView.findViewById(R.id.airline)
        val price: TextView = itemView.findViewById(R.id.price)
        val plane: ImageView = itemView.findViewById(R.id.planeIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketVH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_ticket, parent, false)
        return TicketVH(v)
    }

    override fun getItemCount(): Int = tickets.size

    override fun onBindViewHolder(holder: TicketVH, position: Int) {
        val t = tickets[position]
        holder.depTime.text = t.depTime
        holder.depCode.text = t.depCode
    holder.depCity.text = t.depCity
    holder.depAirport.text = t.depAirport
    holder.arrTime.text = t.arrTime
    holder.arrCode.text = t.arrCode
    holder.arrCity.text = t.arrCity
    holder.arrAirport.text = t.arrAirport
        holder.airline.text = t.airline
        holder.price.text = t.price
    }

    fun update(newList: List<Ticket>) {
        tickets = newList
        notifyDataSetChanged()
    }
}
