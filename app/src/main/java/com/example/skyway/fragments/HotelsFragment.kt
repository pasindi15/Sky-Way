package com.example.skyway.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.skyway.R
import com.example.skyway.adapters.SearchDestinationsAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.skyway.model.SearchDestination

class HotelsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_hotels, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recycler: RecyclerView = view.findViewById(R.id.hotelsRecycler)
        recycler.layoutManager = GridLayoutManager(requireContext(), 2)
        recycler.adapter = SearchDestinationsAdapter(createHotels())
        recycler.setHasFixedSize(true)

        // Profile thumb navigation
        view.findViewById<android.widget.ImageView>(R.id.hotelsProfileThumb)?.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, ProfileFragment())
                .addToBackStack(null)
                .commit()
            activity?.findViewById<BottomNavigationView>(R.id.bottomNavigation)?.selectedItemId = R.id.nav_profile
        }

        // Back navigation
        view.findViewById<View>(R.id.hotelsBack)?.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, HomeFragment())
                .commit()
            activity?.findViewById<BottomNavigationView>(R.id.bottomNavigation)?.selectedItemId = R.id.nav_home
        }
    }

    private fun createHotels(): List<SearchDestination> = listOf(
        SearchDestination("Italy", "Grand Italia Hotel", R.drawable.italy2, "4.7"),
        SearchDestination("Maldives", "Ocean Paradise Resort", R.drawable.maldives, "4.9"),
        SearchDestination("France", "Paris Central Inn", R.drawable.paris2, "4.8"),
        SearchDestination("Sri Lanka", "Colombo Bay Hotel", R.drawable.srilanka, "4.6"),
        SearchDestination("Thailand", "Bangkok Sky Suites", R.drawable.thailand, "4.8"),
        SearchDestination("United Kingdom", "London Royal Stay", R.drawable.uk2, "4.7"),
    )
}
