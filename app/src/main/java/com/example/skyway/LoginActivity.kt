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
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {
    
    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var emailInputLayout: TextInputLayout
    private lateinit var passwordInputLayout: TextInputLayout
    private lateinit var loginButton: Button
    private lateinit var signUpPrompt: TextView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        
        // Hide action bar
        supportActionBar?.hide()
        
        initViews()
        setupSignUpPrompt()
        setupLoginButton()
    }
    
    private fun initViews() {
    emailEditText = findViewById(R.id.emailEditText)
    passwordEditText = findViewById(R.id.passwordEditText)
    emailInputLayout = findViewById(R.id.emailInputLayout)
    passwordInputLayout = findViewById(R.id.passwordInputLayout)
        loginButton = findViewById(R.id.loginButton)
        signUpPrompt = findViewById(R.id.signUpPrompt)
    }
    
    private fun setupSignUpPrompt() {
        val fullText = "${getString(R.string.dont_have_account)}${getString(R.string.sign_up_here)}"
        val spannableString = SpannableString(fullText)
        
        val signUpText = getString(R.string.sign_up_here)
        val startIndex = fullText.indexOf(signUpText)
        val endIndex = startIndex + signUpText.length
        
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
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
        
        signUpPrompt.text = spannableString
        signUpPrompt.movementMethod = LinkMovementMethod.getInstance()
    }
    
    private fun setupLoginButton() {
        val progressBar = findViewById<android.widget.ProgressBar>(R.id.loginProgressBar)
        val loadingText = findViewById<TextView>(R.id.loginLoadingText)
        loginButton.setOnClickListener {
            if (validateInputs()) {
                // Show loading animation and text
                progressBar.visibility = View.VISIBLE
                loadingText.visibility = View.VISIBLE
                loadingText.text = getString(R.string.logging_you_in)
                loginButton.isEnabled = false

                // Simulate network delay
                loginButton.postDelayed({
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }, 1500)
            }
        }
    }
    
    private fun validateInputs(): Boolean {
        var isValid = true
        
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        // Clear previous errors
        emailInputLayout.error = null
        passwordInputLayout.error = null

        // Validate email
        if (email.isEmpty()) {
            emailInputLayout.error = getString(R.string.error_empty_field)
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInputLayout.error = getString(R.string.error_invalid_email)
            isValid = false
        }
        
        // Validate password
        if (password.isEmpty()) {
            passwordInputLayout.error = getString(R.string.error_empty_field)
            isValid = false
        } else if (password.length < 6) {
            passwordInputLayout.error = getString(R.string.error_password_length)
            isValid = false
        }
        
        return isValid
    }
}
