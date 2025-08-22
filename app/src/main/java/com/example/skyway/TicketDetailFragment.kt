package com.example.skyway

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.skyway.model.Ticket

class TicketDetailFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_ticket_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val backBtn: ImageView = view.findViewById(R.id.backBtn)
        backBtn.setOnClickListener { activity?.onBackPressedDispatcher?.onBackPressed() }

        // Retrieve ticket bundle
        val depTime = arguments?.getString("depTime")
        val depCode = arguments?.getString("depCode")
        val arrTime = arguments?.getString("arrTime")
        val arrCode = arguments?.getString("arrCode")

        view.findViewById<TextView>(R.id.detailDepTime).text = depTime
        view.findViewById<TextView>(R.id.detailDepCode).text = depCode
        view.findViewById<TextView>(R.id.detailArrTime).text = arrTime
        view.findViewById<TextView>(R.id.detailArrCode).text = arrCode
    }
}
