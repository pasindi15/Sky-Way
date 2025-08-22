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
import com.example.skyway.model.SearchDestination

class SearchFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recycler: RecyclerView = view.findViewById(R.id.searchDestinationsRecycler)
        recycler.layoutManager = GridLayoutManager(requireContext(), 2)
        recycler.adapter = SearchDestinationsAdapter(createDestinations())
        recycler.setHasFixedSize(true)
    }

    private fun createDestinations(): List<SearchDestination> = listOf(
        SearchDestination("China", "Beijing", R.drawable.search_china, "4.7"),
        SearchDestination("France", "Paris", R.drawable.search_france, "4.9"),
        SearchDestination("Italy", "Rome", R.drawable.search_italy, "4.8"),
        SearchDestination("Switzerland", "Zurich", R.drawable.search_switzerland, "4.6"),
        SearchDestination("Japan", "Tokyo", R.drawable.search_japan, "4.8"),
        SearchDestination("United Kingdom", "London", R.drawable.search_uk, "4.7"),
    )
}
