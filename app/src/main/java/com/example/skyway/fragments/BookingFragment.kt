package com.example.skyway.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.skyway.R
import com.example.skyway.TicketsAdapter
import com.example.skyway.model.Ticket
import com.example.skyway.TicketDetailFragment
import android.widget.EditText
import android.widget.TextView
import android.text.Editable
import android.text.TextWatcher
import android.view.ViewTreeObserver
import android.widget.LinearLayout

class BookingFragment : Fragment() {
    private lateinit var adapter: TicketsAdapter
    private val allTickets = mutableListOf<Ticket>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_booking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ticketsContainer: LinearLayout = view.findViewById(R.id.ticketsContainer)
        // Fallback manual population (not using RecyclerView to keep simple per layout)
        generateTickets()
        populateTickets(ticketsContainer, allTickets)

        val dateFilter: EditText = view.findViewById(R.id.filterDate)
        val paxFilter: EditText = view.findViewById(R.id.filterPassengers)

        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { applyFilters(ticketsContainer, dateFilter.text.toString(), paxFilter.text.toString()) }
            override fun afterTextChanged(s: Editable?) {}
        }
        dateFilter.addTextChangedListener(watcher)
        paxFilter.addTextChangedListener(watcher)
    }

    private fun generateTickets() {
        if (allTickets.isNotEmpty()) return
        val airlines = listOf("Air Canada","Delta","Emirates","Qatar Airways","Lufthansa","Air France","United","British Airways","Turkish","Singapore Air")
    val depCodes = listOf("YUL","JFK","LHR","CDG","FRA","AMS","DXB","DOH","HND","SYD")
    val depCities = listOf("Montreal, Canada","New York, USA","London, UK","Paris, France","Frankfurt, Germany","Amsterdam, NL","Dubai, UAE","Doha, Qatar","Tokyo, Japan","Sydney, Australia")
    val depAirports = listOf("Pierre Elliott Trudeau Intl","John F. Kennedy Intl","Heathrow","Charles de Gaulle","Frankfurt Intl","Schiphol","Dubai Intl","Hamad Intl","Haneda","Kingsford Smith")
    val arrCodes = listOf("YYZ","SFO","DXB","DOH","SIN","HKG","ICN","MEL","KUL","BKK")
    val arrCities = listOf("Toronto, Canada","San Francisco, USA","Dubai, UAE","Doha, Qatar","Singapore","Hong Kong","Seoul, Korea","Melbourne, Australia","Kuala Lumpur, Malaysia","Bangkok, Thailand")
    val arrAirports = listOf("Pearson Intl","SFO Intl","Dubai Intl","Hamad Intl","Changi","Chek Lap Kok","Incheon Intl","Tullamarine","KLIA","Suvarnabhumi")
        for (i in 0 until 10) {
            val depHour = 6 + i
            val arrHour = depHour + 1
            val ticket = Ticket(
                depTime = String.format("%d:%02d %s", ((depHour-1)%12)+1, 5, if (depHour<12) "AM" else "PM"),
                depCode = depCodes[i % depCodes.size],
                depCity = depCities[i % depCities.size],
                depAirport = depAirports[i % depAirports.size],
                arrTime = String.format("%d:%02d %s", ((arrHour-1)%12)+1, 5, if (arrHour<12) "AM" else "PM"),
                arrCode = arrCodes[i % arrCodes.size],
                arrCity = arrCities[i % arrCities.size],
                arrAirport = arrAirports[i % arrAirports.size],
                airline = airlines[i % airlines.size],
                price = "$ ${(1200 + i*50)}"
            )
            allTickets += ticket
        }
    }

    private fun populateTickets(container: LinearLayout, list: List<Ticket>) {
        container.removeAllViews()
        val inflater = LayoutInflater.from(requireContext())
        list.forEach { t ->
            val v = inflater.inflate(R.layout.item_ticket, container, false)
            v.findViewById<TextView>(R.id.departureTime).text = t.depTime
            v.findViewById<TextView>(R.id.departureCode).text = t.depCode
            v.findViewById<TextView>(R.id.departureCity).text = t.depCity
            v.findViewById<TextView>(R.id.departureAirport).text = t.depAirport
            v.findViewById<TextView>(R.id.arrivalTime).text = t.arrTime
            v.findViewById<TextView>(R.id.arrivalCode).text = t.arrCode
            v.findViewById<TextView>(R.id.arrivalCity).text = t.arrCity
            v.findViewById<TextView>(R.id.arrivalAirport).text = t.arrAirport
            v.findViewById<TextView>(R.id.airline).text = t.airline
            v.findViewById<TextView>(R.id.price).text = t.price
            v.setOnClickListener {
                val frag = TicketDetailFragment().apply {
                    arguments = Bundle().apply {
                        putString("depTime", t.depTime)
                        putString("depCode", t.depCode)
                        putString("arrTime", t.arrTime)
                        putString("arrCode", t.arrCode)
                    }
                }
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, frag)
                    .addToBackStack(null)
                    .commit()
            }
            container.addView(v)
        }
    }

    private fun applyFilters(container: LinearLayout, date: String, pax: String) {
        // Simple filter demo: if passengers provided > 0 show subset first N tickets; date currently not altering list but retained for future logic
        val filtered = if (pax.toIntOrNull() != null) allTickets.take(pax.toInt().coerceAtLeast(1).coerceAtMost(allTickets.size)) else allTickets
        populateTickets(container, filtered)
    }
}
