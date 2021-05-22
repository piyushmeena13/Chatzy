package com.example.chatzy.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.chatzy.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.activity_signup.progressBar
import kotlinx.android.synthetic.main.activity_signup.userEmail
import kotlinx.android.synthetic.main.activity_signup.userPassword
import kotlinx.android.synthetic.main.login_activity.*

class SignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        supportActionBar?.hide()
        auth = FirebaseAuth.getInstance()

        signUpBtn.setOnClickListener(View.OnClickListener {
            if(validateUser()){
                progressBar.visibility = View.VISIBLE
                signUpBtn.isEnabled = false
                signUpBtn.isClickable = false
                signUpBtn.alpha = 0.5f
                auth.createUserWithEmailAndPassword(userEmail.text.toString(),userPassword.text.toString())
                    .addOnCompleteListener(this){
                        if (it.isSuccessful){
                            progressBar.visibility = View.GONE

                            val intent = Intent(this, ProfileActivity::class.java).apply {
                                putExtra("Email",userEmail.text.toString()) }
                            startActivity(intent)
                            finish()
                        }else{
                            progressBar.visibility = View.GONE
                            signUpBtn.isEnabled = true
                            signUpBtn.isClickable = true
                            signUpBtn.alpha = 1f
                            Toast.makeText(baseContext, "Authentication failed.",Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        })

        loginTv.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        })
    }

    private fun validateUser():Boolean {
        if(userEmail.length()==0){
            userEmail.setError("Email is required")
            return false
        }
        if(userPassword.length()==0){
            userPassword.setError("Password is required")
            return false
        }
        if(userPassword.length()<6){
            userPassword.setError("Password is weak")
            return false
        }
        if(userPassword.text.toString()!=userConfirmPassword.text.toString()){
            userConfirmPassword.setError("Password Mismatch!")
            return false
        }

        return true
    }
}