package com.example.chatzy.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.chatzy.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.login_activity.*
import kotlinx.android.synthetic.main.login_activity.userEmail
import kotlinx.android.synthetic.main.login_activity.userPassword

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)


        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()
        loginBtn.setOnClickListener(View.OnClickListener {
            if(validateUser()){
                progressBar.visibility = View.VISIBLE
                loginBtn.isEnabled = false
                loginBtn.isClickable = false
                loginBtn.alpha = 0.5f
                auth.signInWithEmailAndPassword(userEmail.text.toString(),userPassword.text.toString())
                    .addOnCompleteListener(this){
                        if(it.isSuccessful){
                            loginFailedTv.visibility = View.GONE
                            progressBar.visibility = View.GONE
                            startActivity(Intent(this, HomeActivity::class.java))
                            finishAffinity()
                        }
                        else{
                            loginFailedTv.visibility = View.VISIBLE
                            progressBar.visibility = View.GONE
                            loginBtn.isEnabled = true
                            loginBtn.isClickable = true
                            loginBtn.alpha = 1f
                        }
                    }
            }
        })

        signUpTv.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
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

        return true
    }

    override fun onStart() {
        super.onStart()
        if(auth.currentUser!=null){
            startActivity(Intent(this, HomeActivity::class.java))
            finishAffinity()
        }
    }
}