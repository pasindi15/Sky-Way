package com.example.skyway

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import android.widget.ArrayAdapter
import com.google.android.material.textfield.MaterialAutoCompleteTextView

class RegisterActivity : AppCompatActivity() {
    
    private lateinit var fullNameEditText: TextInputEditText
    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var confirmPasswordEditText: TextInputEditText
    private lateinit var countryEditText: MaterialAutoCompleteTextView
    private lateinit var termsCheckBox: CheckBox
    
    private lateinit var fullNameInputLayout: TextInputLayout
    private lateinit var emailInputLayout: TextInputLayout
    private lateinit var passwordInputLayout: TextInputLayout
    private lateinit var confirmPasswordInputLayout: TextInputLayout
    private lateinit var countryInputLayout: TextInputLayout
    
    private lateinit var signUpButton: Button
    private lateinit var loginPrompt: TextView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        
        // Hide action bar
        supportActionBar?.hide()
        
        initViews()
        setupCountryDropdown()
        setupLoginPrompt()
        setupSignUpButton()
    }
    
    private fun initViews() {
        fullNameEditText = findViewById(R.id.fullNameEditText)
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText)
    countryEditText = findViewById<MaterialAutoCompleteTextView>(R.id.countryEditText)
        termsCheckBox = findViewById(R.id.termsCheckBox)
        
        fullNameInputLayout = findViewById(R.id.fullNameInputLayout)
        emailInputLayout = findViewById(R.id.emailInputLayout)
        passwordInputLayout = findViewById(R.id.passwordInputLayout)
        confirmPasswordInputLayout = findViewById(R.id.confirmPasswordInputLayout)
        countryInputLayout = findViewById(R.id.countryInputLayout)
        
        signUpButton = findViewById(R.id.signUpButton)
        loginPrompt = findViewById(R.id.loginPrompt)
    }

    private fun setupCountryDropdown() {
        val countries = resources.getStringArray(R.array.countries)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, countries)
        countryEditText.setAdapter(adapter)
        // Enable filtering as user types (MaterialAutoCompleteTextView supports this by default)
        countryEditText.threshold = 1
    }
    
    private fun setupLoginPrompt() {
        val fullText = "${getString(R.string.already_have_account)}${getString(R.string.login_here)}"
        val spannableString = SpannableString(fullText)
        
        val loginText = getString(R.string.login_here)
        val startIndex = fullText.indexOf(loginText)
        val endIndex = startIndex + loginText.length
        
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                finish()
            }
            
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
                ds.color = resources.getColor(R.color.dark_blue, theme)
            }
        }
        
        spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(StyleSpan(android.graphics.Typeface.BOLD), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(UnderlineSpan(), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        
        loginPrompt.text = spannableString
        loginPrompt.movementMethod = LinkMovementMethod.getInstance()
    }
    
    private fun setupSignUpButton() {
        val progressBar = findViewById<android.widget.ProgressBar>(R.id.signupProgressBar)
        val loadingText = findViewById<TextView>(R.id.signupLoadingText)
        signUpButton.setOnClickListener {
            if (validateInputs()) {
                // Show loading animation and text
                progressBar.visibility = View.VISIBLE
                loadingText.visibility = View.VISIBLE
                loadingText.text = getString(R.string.creating_your_account)
                signUpButton.isEnabled = false

                // Simulate network delay
                signUpButton.postDelayed({
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }, 1500)
            }
        }
    }
    
    private fun validateInputs(): Boolean {
        var isValid = true

        val fullName = fullNameEditText.text.toString().trim()
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()
        val confirmPassword = confirmPasswordEditText.text.toString().trim()
        val country = countryEditText.text.toString().trim()

        // Clear previous errors
        fullNameInputLayout.error = null
        emailInputLayout.error = null
        passwordInputLayout.error = null
        confirmPasswordInputLayout.error = null
        countryInputLayout.error = null

        // Validate full name: letters and spaces only
        if (fullName.isEmpty()) {
            fullNameInputLayout.error = getString(R.string.error_empty_field)
            isValid = false
        } else if (!fullName.matches(Regex("^[A-Za-z][A-Za-z ]*$"))) {
            fullNameInputLayout.error = getString(R.string.error_fullname_invalid)
            isValid = false
        }

        // Validate email
        if (email.isEmpty()) {
            emailInputLayout.error = getString(R.string.error_empty_field)
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInputLayout.error = getString(R.string.error_invalid_email)
            isValid = false
        }

        // Validate password: 8+ chars, uppercase, lowercase, digit, symbol
        val strongPassword = password.length >= 8 &&
            password.any { it.isUpperCase() } &&
            password.any { it.isLowerCase() } &&
            password.any { it.isDigit() } &&
            password.any { !it.isLetterOrDigit() }

        if (password.isEmpty()) {
            passwordInputLayout.error = getString(R.string.error_empty_field)
            isValid = false
        } else if (!strongPassword) {
            passwordInputLayout.error = getString(R.string.error_password_requirements)
            isValid = false
        }

        // Validate confirm password
        if (confirmPassword.isEmpty()) {
            confirmPasswordInputLayout.error = getString(R.string.error_empty_field)
            isValid = false
        } else if (password != confirmPassword) {
            confirmPasswordInputLayout.error = getString(R.string.error_password_mismatch)
            isValid = false
        }

        // Validate country
        if (country.isEmpty()) {
            countryInputLayout.error = getString(R.string.error_empty_field)
            isValid = false
        }

        // Validate terms acceptance
        if (!termsCheckBox.isChecked) {
            // Show inline hint near checkbox via TextInputLayout isn't applicable; use content description and Toast
            termsCheckBox.error = "" // shows a small error indicator near the checkbox
            android.widget.Toast.makeText(this, getString(R.string.error_terms_not_accepted), android.widget.Toast.LENGTH_SHORT).show()
            isValid = false
        } else {
            termsCheckBox.error = null
        }

        return isValid
    }
}
