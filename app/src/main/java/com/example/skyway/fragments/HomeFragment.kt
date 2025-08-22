package com.example.skyway.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.skyway.R
// Using adapter in root package due to existing duplicate path
import com.example.skyway.BestOffersAdapter
import com.example.skyway.model.BestOffer
import android.widget.ImageView
import android.widget.LinearLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.skyway.fragments.SearchFragment
import com.example.skyway.FlightBookingFragment
import com.example.skyway.fragments.HotelsFragment

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recycler: RecyclerView = view.findViewById(R.id.bestOffersRecycler)
        recycler.layoutManager = GridLayoutManager(requireContext(), 2)
        recycler.adapter = BestOffersAdapter(createBestOffers())
        recycler.setHasFixedSize(true)

        // Setup explore destination favorites (static views in layout)
        val favoriteIds = listOf(
            R.id.parisFavorite,
            R.id.tokyoFavorite,
            R.id.londonFavorite,
            R.id.athensFavorite,
            R.id.romeFavorite,
            R.id.colomboFavorite,
            R.id.newYorkFavorite,
            R.id.tokyoFavorite2
        )

        favoriteIds.forEach { id ->
            view.findViewById<ImageView>(id)?.let { iv ->
                iv.setOnClickListener {
                    val tagged = (iv.tag as? Boolean) ?: false
                    val newState = !tagged
                    iv.tag = newState
                    iv.setImageResource(if (newState) R.drawable.ic_favorite_selected else R.drawable.ic_favorite)
                }
            }
        }

        // Navigate to Search fragment when Countries icon pressed
        view.findViewById<LinearLayout>(R.id.countriesIconLayout)?.setOnClickListener {
            // Update bottom nav selection so UI stays in sync
            (activity?.findViewById<BottomNavigationView>(R.id.bottomNavigation))?.selectedItemId = R.id.nav_search
            // Fallback direct navigation if selection listener not triggered fast enough
            if (parentFragmentManager.findFragmentById(R.id.fragmentContainer) !is SearchFragment) {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, SearchFragment())
                    .commit()
            }
        }

        // Navigate to Flight Booking when flight icon pressed
        view.findViewById<LinearLayout>(R.id.flightIconLayout)?.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, FlightBookingFragment())
                .addToBackStack(null)
                .commit()
        }

        // Navigate to Hotels when hotel icon pressed
        view.findViewById<LinearLayout>(R.id.hotelIconLayout)?.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, HotelsFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun createBestOffers(): List<BestOffer> = listOf(
        BestOffer("Australia", "Sydney", "$1200", R.drawable.australia),
        BestOffer("Bali", "Denpasar", "$850", R.drawable.bali),
        BestOffer("South Africa", "Cape Town", "$950", R.drawable.south_africa),
        BestOffer("Finland", "Helsinki", "$1100", R.drawable.finland),
        BestOffer("India", "New Delhi", "$700", R.drawable.india),
        BestOffer("Switzerland", "Zurich", "$1500", R.drawable.switzerland),
        BestOffer("Hawaii", "Honolulu", "$1300", R.drawable.hawai),
        BestOffer("Russia", "Moscow", "$1000", R.drawable.russia)
    )
}
