package com.example.skyway

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment

class FlightBookingFragment : Fragment() {

    private lateinit var departureField: AutoCompleteTextView
    private lateinit var arrivalField: AutoCompleteTextView
    private lateinit var departureTimeField: AutoCompleteTextView
    private lateinit var arrivalTimeField: EditText
    private lateinit var passengersField: EditText
    private lateinit var bookButton: TextView
    private lateinit var errorText: TextView
    private lateinit var backBtn: ImageView

    // Provided lists (50 each) with codes
    private val departureLocations = listOf(
        "New York (JFK), USA",
        "Los Angeles (LAX), USA",
        "Chicago (ORD), USA",
        "Miami (MIA), USA",
        "Dallas (DFW), USA",
        "San Francisco (SFO), USA",
        "Seattle (SEA), USA",
        "Denver (DEN), USA",
        "Boston (BOS), USA",
        "Atlanta (ATL), USA",
        "London (LHR), United Kingdom",
        "Paris (CDG), France",
        "Frankfurt (FRA), Germany",
        "Madrid (MAD), Spain",
        "Rome (FCO), Italy",
        "Amsterdam (AMS), Netherlands",
        "Zurich (ZRH), Switzerland",
        "Vienna (VIE), Austria",
        "Lisbon (LIS), Portugal",
        "Brussels (BRU), Belgium",
        "Dubai (DXB), UAE",
        "Doha (DOH), Qatar",
        "Istanbul (IST), Turkey",
        "Singapore (SIN), Singapore",
        "Hong Kong (HKG), China (SAR)",
        "Tokyo (HND), Japan",
        "Osaka (KIX), Japan",
        "Seoul (ICN), South Korea",
        "Bangkok (BKK), Thailand",
        "Kuala Lumpur (KUL), Malaysia",
        "Sydney (SYD), Australia",
        "Melbourne (MEL), Australia",
        "Auckland (AKL), New Zealand",
        "Toronto (YYZ), Canada",
        "Vancouver (YVR), Canada",
        "Montreal (YUL), Canada",
        "Mexico City (MEX), Mexico",
        "São Paulo (GRU), Brazil",
        "Buenos Aires (EZE), Argentina",
        "Santiago (SCL), Chile",
        "Johannesburg (JNB), South Africa",
        "Cape Town (CPT), South Africa",
        "Nairobi (NBO), Kenya",
        "Cairo (CAI), Egypt",
        "Delhi (DEL), India",
        "Mumbai (BOM), India",
        "Colombo (CMB), Sri Lanka",
        "Karachi (KHI), Pakistan",
        "Manila (MNL), Philippines",
        "Jakarta (CGK), Indonesia"
    )

    private val arrivalLocations = listOf(
        "Las Vegas (LAS), USA",
        "Orlando (MCO), USA",
        "Phoenix (PHX), USA",
        "Houston (IAH), USA",
        "Philadelphia (PHL), USA",
        "San Diego (SAN), USA",
        "Portland (PDX), USA",
        "Charlotte (CLT), USA",
        "Detroit (DTW), USA",
        "Minneapolis (MSP), USA",
        "Manchester (MAN), United Kingdom",
        "Dublin (DUB), Ireland",
        "Berlin (BER), Germany",
        "Munich (MUC), Germany",
        "Athens (ATH), Greece",
        "Oslo (OSL), Norway",
        "Stockholm (ARN), Sweden",
        "Helsinki (HEL), Finland",
        "Prague (PRG), Czech Republic",
        "Warsaw (WAW), Poland",
        "Riyadh (RUH), Saudi Arabia",
        "Jeddah (JED), Saudi Arabia",
        "Muscat (MCT), Oman",
        "Beijing (PEK), China",
        "Shanghai (PVG), China",
        "Shenzhen (SZX), China",
        "Guangzhou (CAN), China",
        "Ho Chi Minh City (SGN), Vietnam",
        "Hanoi (HAN), Vietnam",
        "Phnom Penh (PNH), Cambodia",
        "Perth (PER), Australia",
        "Brisbane (BNE), Australia",
        "Adelaide (ADL), Australia",
        "Ottawa (YOW), Canada",
        "Calgary (YYC), Canada",
        "Edmonton (YEG), Canada",
        "Rio de Janeiro (GIG), Brazil",
        "Lima (LIM), Peru",
        "Bogotá (BOG), Colombia",
        "Quito (UIO), Ecuador",
        "Durban (DUR), South Africa",
        "Addis Ababa (ADD), Ethiopia",
        "Casablanca (CMN), Morocco",
        "Lagos (LOS), Nigeria",
        "Bangalore (BLR), India",
        "Hyderabad (HYD), India",
        "Chennai (MAA), India",
        "Male (MLE), Maldives",
        "Phuket (HKT), Thailand",
        "Denpasar – Bali (DPS), Indonesia"
    )

    // Dynamically generate departure time hours (24h) and assign random arrival offsets (1–5h)
    private val timeMap: Map<String, String> by lazy {
        val map = mutableMapOf<String, String>()
        val rand = java.util.Random()
        for (h in 0 until 24) {
            val dep = String.format("%02d:00", h)
            val offset = 1 + rand.nextInt(5) // 1..5 hours
            val arrHour = (h + offset) % 24
            val arr = String.format("%02d:%02d", arrHour, 0)
            map[dep] = arr
        }
        map
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_flight_booking, container, false)
        bindViews(view)
        setupAutoComplete()
        setupListeners()
        return view
    }

    private fun bindViews(view: View) {
        departureField = view.findViewById(R.id.departureField)
        arrivalField = view.findViewById(R.id.arrivalField)
        departureTimeField = view.findViewById(R.id.departureTimeField)
        arrivalTimeField = view.findViewById(R.id.arrivalTimeField)
        passengersField = view.findViewById(R.id.passengersField)
        bookButton = view.findViewById(R.id.bookFlightButton)
        errorText = view.findViewById(R.id.errorText)
        backBtn = view.findViewById(R.id.backBtn)
    }

    private fun setupAutoComplete() {
    val depAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, departureLocations)
    departureField.setAdapter(depAdapter)
    val arrAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, arrivalLocations)
    arrivalField.setAdapter(arrAdapter)
    arrivalField.keyListener = null // make uneditable except via dropdown selection

    val timeAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, timeMap.keys.toList())
    departureTimeField.setAdapter(timeAdapter)

        // Open dropdown on focus or click for better UX
        listOf(departureField, arrivalField, departureTimeField).forEach { actv ->
            actv.setOnClickListener { actv.showDropDown() }
            actv.setOnFocusChangeListener { v, hasFocus -> if (hasFocus) (v as? AutoCompleteTextView)?.showDropDown() }
        }
    }

    private fun setupListeners() {
        departureTimeField.setOnItemClickListener { _, _, _, _ ->
            val dep = departureTimeField.text.toString()
            val arr = timeMap[dep]
            arrivalTimeField.setText(arr ?: "")
            validateForm()
        }
        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { validateForm() }
            override fun afterTextChanged(s: Editable?) { }
        }
        departureField.addTextChangedListener(watcher)
        arrivalField.addTextChangedListener(watcher)
        passengersField.addTextChangedListener(watcher)

        bookButton.setOnClickListener { attemptBooking() }
        backBtn.setOnClickListener { activity?.onBackPressedDispatcher?.onBackPressed() }
    }

    private fun validateForm() {
        val dep = departureField.text.toString().trim()
        val arr = arrivalField.text.toString().trim()
        val depTime = departureTimeField.text.toString().trim()
        val arrTime = arrivalTimeField.text.toString().trim()
        val pax = passengersField.text.toString().trim()

        val valid = dep.isNotEmpty() && arr.isNotEmpty() && depTime.isNotEmpty() && arrTime.isNotEmpty() && pax.toIntOrNull()?.let { it > 0 } == true
        bookButton.isEnabled = valid
        bookButton.alpha = if (valid) 1f else 0.5f
        if (valid) errorText.visibility = View.GONE
    }

    private fun attemptBooking() {
        val paxNum = passengersField.text.toString().toIntOrNull()
        if (paxNum == null || paxNum <= 0) {
            showError("Enter valid passengers")
            return
        }
        if (departureField.text.toString() == arrivalField.text.toString()) {
            showError("Departure and arrival can't match")
            return
        }
        Toast.makeText(requireContext(), "Flight booked!", Toast.LENGTH_SHORT).show()
        // Go straight to BookingFragment (existing booking screen)
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, com.example.skyway.fragments.BookingFragment())
            .addToBackStack(null)
            .commit()
    }

    private fun showError(msg: String) {
        errorText.text = msg
        errorText.visibility = View.VISIBLE
    }
}
