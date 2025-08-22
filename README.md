<!--
*******************************************************************************************************************
 SKYWAY - EXTENSIVE VIVA / ORAL EXAMINATION README
 Purpose: Provide a deep, pedagogical, example‑rich walkthrough (500+ lines) covering every implemented feature,
					architectural decision, UI pattern, resource usage, and typical viva questions & answers.
*******************************************************************************************************************
-->

# SkyWay ✈️ – Comprehensive Viva / Oral Defense Documentation

> “SkyWay” is a modular Android application demonstrating a modern travel / flight booking experience using Fragments,
> reusable card components, form validation, dynamic data generation, resource centralization, and clean separation of
> UI concerns. This README is intentionally long and tutorial‑style to help you explain design choices in a viva.

---
## TABLE OF CONTENTS
1. Executive Overview
2. Feature Inventory & User Journey
3. High‑Level Architecture & Module Responsibilities
4. Directory / Package Structure Map
5. Fragment Lifecycle & Navigation Strategy
6. Screen Deep Dives (Splash → Ticket Detail)
7. Layout & View System Patterns (LinearLayout, RelativeLayout, ScrollView, RecyclerView, Manual Inflation)
8. Card Components & Reusability
9. Grid & List Implementations (Destination Grid, Offers List, Tickets)
10. Forms & Validation Mechanics (Login, Signup, Booking)
11. Dynamic Data Generation (Time Offsets, Ticket Samples)
12. Resource Management (Strings, Colors, Dimensions, Arrays, Drawables)
13. Theming & Gradients
14. Favorite (Like/Heart) State System
15. Ticket Detail Composition & Information Hierarchy
16. Adapter Patterns & View Binding Samples
17. State Handling & UI Feedback (Progress Indicators, Error Text)
18. Animations / UX Refinements (Current + Potential)
19. Performance Considerations & Future Scaling
20. Error Handling Strategies
21. Accessibility & Internationalization Readiness
22. Testing Strategy (Planned & Suggested Tests)
23. Security & Input Sanitization Considerations
24. Extensibility Roadmap
25. Common Viva Q&A (with Suggested Answers)
26. Code Snippet Appendix (Grouped by Topic)
27. Glossary of Terms
28. Summary / Closing Statement

---
## 1. Executive Overview
SkyWay showcases how to structure a multi‑screen, fragment‑driven Android UI without heavy frameworks while following
clean resource usage. It aims to be: (1) Understandable to juniors, (2) Extensible for advanced features, (3) Demonstrative
for viva evaluation (patterns, rationale, modularity).

Key Pillars:
* Fragment‑centric modularity instead of single Activity or huge navigation graph.
* Declarative resource strategy (colors, strings) to enable localization & theming.
* Clear separation between UI generation (adapters / fragment code) and raw resources.
* Progressive enhancement approach—manual prototypes (manual ticket inflation) that can later convert to RecyclerView.

---
## 2. Feature Inventory & User Journey

| Step | Screen | Core Purpose | Key Elements | Data Flow |
|------|--------|--------------|--------------|----------|
| 1 | Splash / Get Started | Brand entry, CTA | Gradient, logo, Login / Sign Up buttons | Static resources |
| 2 | Login | Authentication input | Email/password fields, validation | User input → validation logic |
| 3 | Sign Up | Account creation | Extended form + country dropdown + terms | Input → multi rules |
| 4 | Home | Hub & discovery | Greeting, quick access, explore cards, offers list, favorites | In‑memory offer list |
| 5 | Search Destinations | Browsing + liking | Grid of destinations, heart toggle | Adapter list state |
| 6 | Flight Booking | Trip specification | Autocomplete departure/arrival, time selection, passengers | Dynamic time map generation |
| 7 | Booking (Tickets) | List of ticket options | Ticket cards, filter placeholders | Dynamic ticket objects |
| 8 | Ticket Detail | Detailed itinerary | Airline/date, times, meta, passenger, QR, print | Bundle arguments |
| 9 | Notifications | Informational feed | Static cards (title/body/time) | Static list |
| 10 | Profile | Identity & actions | Avatar, name/email, settings list | Static + placeholders |

User Flow Narrative: A new user opens Splash, registers via Sign Up, lands on Home, explores destinations, books a flight
through the Booking form, views generated tickets, and inspects details.

---
## 3. High‑Level Architecture & Module Responsibilities

```
Activity (Host)
	└── Fragment Container (dynamic) --------------------------------------------------------------
			 | HomeFragment            – Aggregates quick access + lists
			 | SearchFragment          – Destination grid & favorites
			 | FlightBookingFragment   – Form & dynamic validation/time logic
			 | BookingFragment         – Ticket list (manual inflation, navigation to detail)
			 | TicketDetailFragment    – Detailed card
			 | NotificationsFragment   – Static notification feed
			 | ProfileFragment         – Profile & settings
			 | (LoginActivity, RegisterActivity, SplashActivity as standalone entry points)

Resources Layer (res/): colors, strings, drawables, arrays, layouts, styles
Models (data classes): BestOffer, SearchDestination, Ticket (lightweight, UI-focused)
Adapters: OfferAdapter, SearchDestinationsAdapter (patterns reused conceptually for future tickets)
Navigation: Manual fragment transactions; argument Bundles for Ticket detail
```

Design Choice Explanation:
* Avoids over‑abstraction early (no unnecessary MVVM framework), enabling faster iteration.
* Logical separation—each fragment owns a single concern.
* Data models purposely minimal to discourage premature coupling with backend assumptions.

---
## 4. Directory / Package Structure Map

```
app/src/main/java/...
	ui/
		activities/ (SplashActivity, LoginActivity, RegisterActivity)
		fragments/  (HomeFragment, SearchFragment, FlightBookingFragment, BookingFragment, TicketDetailFragment, ...)
		adapters/   (BestOffersAdapter, SearchDestinationsAdapter, TicketsAdapter [future or partial])
		models/     (BestOffer.kt, SearchDestination.kt, Ticket.kt)

res/
	layout/       (activity_*.xml, fragment_*.xml, item_*.xml)
	drawable/     (gradients, shapes, icons, ticket assets)
	values/       (strings.xml, colors.xml, arrays.xml, dimens.xml, styles.xml)
```

Rationale: Flat, discoverable structure for viva explanation. New developers can trace UI from layout → fragment → adapter.

---
## 5. Fragment Lifecycle & Navigation Strategy

### 5.1 Lifecycle Touchpoints
Common overrides leveraged:
* `onCreateView` – Inflate layout.
* `onViewCreated` – Bind views, set listeners, initialize adapters.
* `onDestroyView` – (Could release view bindings if using binding library; optional here.)

### 5.2 Manual Navigation Helper
```kotlin
fun Fragment.navigateTo(next: Fragment) {
		parentFragmentManager.beginTransaction()
				.replace(R.id.fragmentContainer, next)
				.addToBackStack(null)
				.commit()
}
```
Advantages (for viva):
1. Teaches raw fragment transaction mechanism.
2. Fewer files than Navigation Component for small scope.
3. Back stack control performed explicitly.

### 5.3 Argument Passing (Bundle)
```kotlin
val detail = TicketDetailFragment().apply {
		arguments = bundleOf(
				"DEP_CODE" to ticket.depCode,
				"ARR_CODE" to ticket.arrCode,
				"DEP_TIME" to ticket.depTime,
				"ARR_TIME" to ticket.arrivalTime,
				"PRICE" to ticket.price
		)
}
navigateTo(detail)
```
In `TicketDetailFragment.onViewCreated` retrieve via `arguments?.getString("DEP_CODE")` etc.

---
## 6. Screen Deep Dives

### 6.1 Splash / Get Started
* Minimal waiting—no artificial delay.
* Gradient background from `gradient_background` drawable.
* CTA Buttons → `LoginActivity` / `RegisterActivity`.

### 6.2 Login Activity
* Inputs: Email, Password.
* Password toggle via `TextInputLayout` end icon.
* Progress UI appears **below** the button (recent change) when authenticating.
* Validation ensures email pattern and length requirements.

### 6.3 Register Activity
* Adds: Full Name, Confirm Password, Country (`MaterialAutoCompleteTextView`), Terms checkbox.
* Stepwise validation: stop at first blocking error to guide user.
* Country suggestions from `arrays.xml`.

### 6.4 Home Fragment
* Greeting & subtitle (strings centralized).
* Quick access icons (Flights, Hotel, Countries, Train) with consistent circular background shape.
* Explore Destinations horizontal card list: images + rating + country.
* Best Offers list with heart toggle storing `isFavorite` in model.

### 6.5 Search Fragment
* Grid (2 columns) of `SearchDestination` items.
* Favorites toggled in adapter, visually immediate.
* Scrollable content with header & banner for context.

### 6.6 Flight Booking Fragment
* Autocomplete departure & arrival (50+ sample entries each) improves UX vs free text.
* Dep Time dropdown list (24hr) → arrival auto offset (random or computed difference).
* Arrival time set read‑only to prevent mismatch editing.
* Passenger count numeric with validation.
* After booking, directly navigates to BookingFragment (confirmation bypassed per requirement adjustment).

### 6.7 Booking Fragment
* Banner + filter placeholders (future extension: price, airline, departure time filters).
* Manual ticket card inflation demonstrates dynamic data binding w/out adapter overhead (teaching pattern).
* Each card: departure / arrival block, airline, price.
* Tap navigates to ticket detail.

### 6.8 Ticket Detail Fragment
* White card aesthetic for readability over gradient; structured vertical sections.
* Divider between departure & arrival data improves scanning.
* Meta row uniform column weights maintain balanced layout.
* Passenger identity & QR code support real‑world boarding concept.
* Print button styled with grey gradient and icon—action affordance.

### 6.9 Notifications Fragment
* Static prototypes for potential push/remote notifications.
* Each card: title, body, relative time.

### 6.10 Profile Fragment
* Avatar (circular), name, email, settings actions (placeholders for further navigation).
* Consistent white card list pattern.

---
## 7. Layout & View System Patterns

Pattern | Description | Example Benefit | Example File
--------|-------------|-----------------|-------------
Linear (Vertical) | Stacked inputs & sections | Predictable vertical flow | `activity_login.xml`
Linear (Horizontal) | Row meta distribution | Even column semantics | Ticket meta row
RelativeLayout | Overlapping / relative constraints | Position QR, back button | `fragment_ticket_detail.xml`
ScrollView | Ensure accessibility on small displays | Prevent clipped forms | `activity_register.xml`
RecyclerView | Efficient scroll lists (recyclers) | Memory efficiency | Destination grid
Manual Inflate | Quick prototyping of static count cards | Lower code overhead | `BookingFragment.kt`

### 7.1 Weighted Columns
Use `android:layout_weight="1"` to ensure equal column distribution without manual width calculations.

### 7.2 Divider View
Central vertical `View` (1dp width) creates visual separation for departure/arrival groups.

---
## 8. Card Components & Reusability
Core styling encapsulated in shape drawables (rounded corners, white fill). Cards vary only by internal content, promoting
consistency and implementing a systematic design language.

### 8.1 Example Shape (Simplified)
```xml
<shape xmlns:android="http://schemas.android.com/apk/res/android" android:shape="rectangle">
		<solid android:color="@android:color/white" />
		<corners android:radius="20dp" />
</shape>
```

---
## 9. Grid & List Implementations

### 9.1 Destination Grid Adapter (Conceptual Example)
```kotlin
class SearchDestinationsAdapter(
		private val items: MutableList<SearchDestination>,
		private val onFavoriteToggle: (SearchDestination) -> Unit
) : RecyclerView.Adapter<SearchDestinationsAdapter.VH>() {

		inner class VH(view: View) : RecyclerView.ViewHolder(view) {
				val heart: ImageView = view.findViewById(R.id.favoriteIcon)
				val title: TextView = view.findViewById(R.id.cityName)
				fun bind(model: SearchDestination) {
						title.text = model.city
						heart.setImageResource(if (model.isFavorite) R.drawable.ic_favorite_selected else R.drawable.ic_favorite_outline)
						heart.setOnClickListener {
								model.isFavorite = !model.isFavorite
								notifyItemChanged(bindingAdapterPosition)
								onFavoriteToggle(model)
						}
				}
		}

		override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
				VH(LayoutInflater.from(parent.context).inflate(R.layout.item_search_destination, parent, false))

		override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(items[position])
		override fun getItemCount(): Int = items.size
}
```

### 9.2 Manual Ticket Inflation Rationale
Used to teach raw view inflation & data binding quickly:
```kotlin
repeat(10) { index ->
		val card = layoutInflater.inflate(R.layout.item_ticket, ticketsContainer, false)
		card.findViewById<TextView>(R.id.depCode).text = sample.depCode
		// ... bind rest
		card.setOnClickListener { openTicketDetail(sample) }
		ticketsContainer.addView(card)
}
```
Migration path: Replace `LinearLayout` container with `RecyclerView` once dataset grows.

---
## 10. Forms & Validation Mechanics

### 10.1 Validation Helper Pattern
```kotlin
fun validateEmail(edit: TextInputEditText, layout: TextInputLayout): Boolean {
		val value = edit.text?.trim().orEmpty()
		return if (!Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
				layout.error = edit.context.getString(R.string.error_invalid_email)
				false
		} else { layout.error = null; true }
}
```

### 10.2 Password Complexity (Illustrative)
```kotlin
val valid = password.length >= 8 &&
						password.any { it.isUpperCase() } &&
						password.any { it.isLowerCase() } &&
						password.any { it.isDigit() } &&
						password.any { !it.isLetterOrDigit() }
```

### 10.3 Booking Form Flow
1. User selects departure → show filtered list.
2. Select departure time → compute arrival offset & set arrival field.
3. Validate passenger count (>0).
4. On success, navigate to `BookingFragment` (skip intermediate confirmation per requirement tweak).

---
## 11. Dynamic Data Generation

### 11.1 Time Mapping
```kotlin
val timeSlots = (0 until 24).flatMap { hour -> listOf(0,30).map { m -> String.format("%02d:%02d", hour, m) }}
departureAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, timeSlots)

fun autoArrival(dep: String): String {
		val (h,m) = dep.split(":").map { it.toInt() }
		val total = h * 60 + m + (60..180).random() // +1‑3h random offset
		val nh = (total / 60) % 24
		val nm = total % 60
		return String.format("%02d:%02d", nh, nm)
}
```

### 11.2 Ticket Sample Generation
```kotlin
data class Ticket(
	val depCode:String, val arrCode:String, val depTime:String, val arrivalTime:String, val price:Int
)

val tickets = (1..10).map {
	val dep = timeSlots.random()
	val arr = autoArrival(dep)
	Ticket("YUL","YYZ", dep, arr, (400..900).random())
}
```

---
## 12. Resource Management

| Resource Type | Purpose | Example |
|---------------|---------|---------|
| strings.xml | User‑facing text | `error_invalid_email` |
| colors.xml | Semantic palette | `gradient_start` / `error_red` |
| arrays.xml | Country autocomplete | `countries` array |
| dimens.xml | Spacing & sizes (future extension) | `card_corner_radius` |
| drawables | Shapes, gradients, icons | `print_button_background` |

Centralization reduces duplication & eases localization (translate only `strings.xml`).

---
## 13. Theming & Gradients
Background gradient conveys brand identity. Buttons reuse darker gradient variant to maintain contrast hierarchy.
Potential extension: implement dark theme by inverting palette variables only.

---
## 14. Favorite (Heart) State System
Structure:
```kotlin
data class BestOffer(val id:Int, val title:String, var isFavorite:Boolean = false)
```
UI toggles the boolean and triggers `notifyItemChanged`. (Could escalate to persistence later.)

---
## 15. Ticket Detail Composition & Hierarchy
1. Airline + Date: Immediate context.
2. Departure vs Arrival: Mirrored columns, easy mental mapping.
3. Meta Row: Dense info cluster (class/gate/terminal/flight) with labels subdued (`@android:color/darker_gray`).
4. Passenger Info: Ownership & identity.
5. QR Code: Boarding token visual.
6. Print Button: Actionable conclusion.

Design heuristics: Bold for primary numbers/times, lighter secondary labels, generous vertical spacing (16–24dp) to avoid clutter.

---
## 16. Adapter Patterns & View Binding Samples
### 16.1 BestOffersAdapter (Conceptual)
```kotlin
class BestOffersAdapter(
	private val data: MutableList<BestOffer>
) : RecyclerView.Adapter<BestOffersAdapter.VH>() {

	inner class VH(view: View): RecyclerView.ViewHolder(view) {
		 private val heart = view.findViewById<ImageView>(R.id.heart)
		 private val title = view.findViewById<TextView>(R.id.offerTitle)
		 fun bind(item: BestOffer) {
				title.text = item.title
				heart.setImageResource(if (item.isFavorite) R.drawable.ic_favorite_selected else R.drawable.ic_favorite_outline)
				heart.setOnClickListener {
					 item.isFavorite = !item.isFavorite
					 notifyItemChanged(bindingAdapterPosition)
				}
		 }
	}

	override fun onCreateViewHolder(p: ViewGroup, vt:Int) = VH(LayoutInflater.from(p.context).inflate(R.layout.item_best_offer, p,false))
	override fun getItemCount() = data.size
	override fun onBindViewHolder(h: VH, pos:Int) = h.bind(data[pos])
}
```

---
## 17. State Handling & UI Feedback
* Loading states: Show progress + text (`loginProgressBar`, `loginLoadingText`) after button pressed.
* Disabled interactions (future) could grey out fields while loading.
* Consistent error messaging via `TextInputLayout.error`.

---
## 18. Animations / UX Refinements
Current Minimal Animations:
* Instant feedback on favorite toggle.
Potential Additions (explain in viva):
* Scale animation on heart tap (`ViewPropertyAnimator.scaleX/Y`).
* Shared element transition from ticket card → detail (image/time text).
* Fade in list items using `LayoutAnimationController`.
* Lottie on splash for brand reinforcement (kept out for simplicity now).

---
## 19. Performance Considerations & Future Scaling
Topic | Current Approach | Scaling Path
------|------------------|-------------
Ticket List | Manual inflation | Convert to RecyclerView + DiffUtil
Favorites Persistence | In‑memory boolean | Persist in Room or DataStore
Large Image Handling | Local drawables | Glide/Coil with caching for remote images
Form Validation | Inline sequential | Extract validators into utility module / unit tests

---
## 20. Error Handling Strategies
Current focus on validation & safe UI binding. Potential future layers:
* Network error domain (if API integrated) with sealed classes: `Success`, `NetworkError`, `ServerError`.
* Centralized logger for analytics.
* Retry strategies for booking operations.

---
## 21. Accessibility & Internationalization Readiness
Implemented:
* Content descriptions on critical icons (Back, QR, Profile) can be audited.
* All text centralized for translation (strings.xml) except still‑placeholder times / codes.
To Improve:
* Larger touch targets (48dp min) check.
* TalkBack traversal order review.
* High contrast mode adaptation & dark theme variant.

---
## 22. Testing Strategy (Suggested)
Test Type | Target | Rationale
----------|--------|----------
Unit | Time auto‑arrival logic | Correct scheduling
Unit | Password validator | Ensures rule evolution safety
UI (Espresso) | Login → Home flow | Regression guard
UI (Espresso) | Booking to Ticket Detail | Ensure bundle integrity
Instrumentation | Rotation handling (if added) | State retention

Pseudo unit test example:
```kotlin
@Test fun arrivalOffsetWithinBounds() {
	repeat(100) {
		val arr = autoArrival("08:00")
		// parse arr, ensure difference between 60..180 minutes
	}
}
```

---
## 23. Security & Input Sanitization Considerations
Current scope is offline demo; still highlight in viva:
* Sanitize inputs (strip leading/trailing spaces, restrict scripts).
* Use HTTPS & token storage (future backend).
* Avoid logging sensitive data (passwords) in production.

---
## 24. Extensibility Roadmap
1. **API Integration:** Real flight search + caching.
2. **User Accounts:** Persist profile, favorites server‑side.
3. **Payments:** Add secure payment fragment with PCI considerations.
4. **Offline Mode:** Cache last viewed tickets / destinations.
5. **Notifications:** Real push (FCM) replacing static list.
6. **Theming:** Automatic dark / dynamic color (Material You).
7. **Analytics:** Screen & event logging.
8. **Testing:** CI pipeline executing validation & UI smoke tests.

---
## 25. Common Viva Q&A
Q: Why manual fragment transactions instead of Navigation Component?
A: Simplicity and explicit control; scope small; avoids extra graph files while teaching fundamentals.

Q: How would you persist favorite state?
A: Introduce Room entity (id, isFavorite) + DAO; on toggle update DB; observe via Flow / LiveData.

Q: How to scale ticket list for hundreds of items?
A: Replace manual inflation with RecyclerView + DiffUtil + Paging (if remote source) + view holder binding optimizations.

Q: What is the advantage of centralizing strings?
A: Localization, consistency, reuse; boundaries only change once—reduces regression risk.

Q: How ensure accessibility improvements?
A: Add content descriptions, test with TalkBack, maintain color contrast ratios, support font scaling via `sp` units.

Q: Future improvement for validation architecture?
A: Extract validators into pure Kotlin functions + unit tests; integrate form state sealed class for better composability.

Q: Why not MVVM now?
A: Overhead vs educational clarity—data is static; when adding network/persistence, introduce ViewModels.

Q: How to add dark mode quickly?
A: Duplicate colors.xml → values-night/colors.xml overriding palette tokens (no layout changes required).

Q: Mitigate performance cost of complex gradients?
A: Cache backgrounds, reduce overdraw (use single root gradient), leverage theme attributes where possible.

Q: Strategy for printing ticket?
A: Use Android Print Framework or generate PDF via `PdfDocument`, feed boarded data & QR into template.

---
## 26. Code Snippet Appendix (Grouped)

### 26.1 Fragment Navigation Helper
```kotlin
fun Fragment.navigateTo(f: Fragment) {
	parentFragmentManager.beginTransaction()
		.replace(R.id.fragmentContainer, f)
		.addToBackStack(null)
		.commit()
}
```

### 26.2 Simple Heart Toggle
```kotlin
heartImage.setOnClickListener {
	model.isFavorite = !model.isFavorite
	heartImage.setImageResource(if (model.isFavorite) R.drawable.ic_favorite_selected else R.drawable.ic_favorite_outline)
}
```

### 26.3 Autocomplete Setup
```kotlin
val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, countries)
departureField.setAdapter(adapter)
arrivalField.setAdapter(adapter)
```

### 26.4 Time Dropdown Generation
```kotlin
val hourSlots = buildList {
	for (h in 0 until 24) for (m in listOf(0,30)) add(String.format("%02d:%02d", h, m))
}
timeAutoComplete.setAdapter(ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, hourSlots))
```

### 26.5 Resource Referencing Patterns
```xml
<TextView
	android:text="@string/booking"
	android:textColor="@color/dark_blue"
	android:padding="@dimen/space_medium" />
```

### 26.6 Shape Drawable (Grey Gradient Button)
```xml
<shape xmlns:android="http://schemas.android.com/apk/res/android" android:shape="rectangle">
	<gradient android:startColor="#B0B0B0" android:endColor="#7A7A7A" android:angle="0" />
	<corners android:radius="28dp"/>
</shape>
```

### 26.7 Bundle Argument Retrieval
```kotlin
val depCode = arguments?.getString("DEP_CODE") ?: "--"
depCodeView.text = depCode
```

### 26.8 Validation Error Display
```kotlin
fun TextInputLayout.fail(msgRes:Int) { error = context.getString(msgRes) }
```

### 26.9 Potential RecyclerView Migration for Tickets
```kotlin
class TicketsAdapter(private val data: List<Ticket>): RecyclerView.Adapter<TicketsAdapter.VH>() {
	inner class VH(v:View):RecyclerView.ViewHolder(v) { /* bind similar to others */ }
	// create + bind omitted for brevity
}
```

### 26.10 Random Arrival Offset Utility
```kotlin
fun randomArrival(depMinutes:Int):Int = depMinutes + (60..180).random()
```

---
## 27. Glossary of Terms
Term | Definition
-----|-----------
Fragment | Modular UI/controller component hosted by an Activity.
RecyclerView | Efficient, flexible list/grid container with view recycling.
Adapter | Binds data set items to RecyclerView item views.
Bundle | Key/value map used to pass primitive data between Android components.
Resource | Externalized asset (string, color, drawable) referenced via generated IDs.
Validation | Ensuring user input meets defined rules before processing.

---
## 28. Summary / Closing Statement
SkyWay is a pedagogical showcase balancing clarity and extensibility. It demonstrates multiple UI paradigms (lists, grids,
forms, detail screens) while emphasizing resource centralization and modular fragments. The code is intentionally
approachable, providing a scaffold for layering in advanced patterns (MVVM, repository layer, navigation component,
networking, persistence, theming) as project scope expands.

End of Viva Documentation.

