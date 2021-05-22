package com.example.chatzy.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.chatzy.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        supportActionBar?.hide()

        val Name = intent.getStringExtra("userName")
        val Email = intent.getStringExtra("userEmail")
        val Profile = intent.getStringExtra("userProfile")

        Glide.with(this@SettingsActivity)
            .load(Profile)
            .into(userImage)
        username.setText(Name)
        useremail.setText(Email)

        backBtn.setOnClickListener(View.OnClickListener {
            super.onBackPressed()
        })
        logoutBtn.setOnClickListener(View.OnClickListener {
            var dialog  = AlertDialog.Builder(this)
                .setTitle("Are you sure you want to signout?")
                .setPositiveButton("Yes!"){dialog,which ->
                    var auth= FirebaseAuth.getInstance()
                    auth.signOut()
                    var intent = Intent(this,LoginActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                }
                .setNegativeButton("NO"){dialog,which ->
                    dialog.dismiss()
                }
                dialog.show()
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}