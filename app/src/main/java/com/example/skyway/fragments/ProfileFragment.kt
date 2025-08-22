package com.example.skyway.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.skyway.R

class ProfileFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        val logoutLayout = view.findViewById<View>(R.id.logoutLayout)
        logoutLayout?.setOnClickListener {
            requireActivity().startActivity(android.content.Intent(requireContext(), com.example.skyway.GetStartedActivity::class.java))
            requireActivity().finish()
        }
        return view
    }
}
