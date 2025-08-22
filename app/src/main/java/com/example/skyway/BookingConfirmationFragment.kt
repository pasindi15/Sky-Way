package com.example.skyway

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class BookingConfirmationFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_booking_confirmation, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val routeText: TextView = view.findViewById(R.id.routeText)
        val timeText: TextView = view.findViewById(R.id.timeText)
        val passengersText: TextView = view.findViewById(R.id.passengersText)
        val doneButton: TextView = view.findViewById(R.id.doneButton)

        val dep = arguments?.getString("dep") ?: ""
        val arr = arguments?.getString("arr") ?: ""
        val depTime = arguments?.getString("depTime") ?: ""
        val arrTime = arguments?.getString("arrTime") ?: ""
        val pax = arguments?.getString("pax") ?: ""

        routeText.text = "$dep → $arr"
        timeText.text = "$depTime → $arrTime"
        passengersText.text = "Passengers: $pax"

        doneButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}
