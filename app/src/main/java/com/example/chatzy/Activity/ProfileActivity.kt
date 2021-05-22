package com.example.chatzy.Activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.chatzy.Models.UserModel
import com.example.chatzy.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.progressBar
import kotlinx.android.synthetic.main.login_activity.*

class ProfileActivity : AppCompatActivity() {
    private var imageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)


        val auth=FirebaseAuth.getInstance()
        val database=FirebaseDatabase.getInstance()
        val storage= FirebaseStorage.getInstance()

        supportActionBar?.hide()

        userImage.setOnClickListener(View.OnClickListener {
            val intent = Intent().setAction(Intent.ACTION_GET_CONTENT)
                .setType("image/*")
            startActivityForResult(intent,13)
        })

        createAccountBtn.setOnClickListener(View.OnClickListener {
            if(validateUser())
            {
                progressBar.visibility = View.VISIBLE
                createAccountBtn.isEnabled = false
                createAccountBtn.isClickable = false
                createAccountBtn.alpha = 0.5f
                val userEmail =intent.getStringExtra("Email")
                val userName = username.text.toString()
                val reference = storage.getReference().child("profiles").child(auth.uid.toString())
                reference.putFile(imageUri!!).addOnCompleteListener(OnCompleteListener {
                    if(it.isSuccessful)
                    {
                        reference.downloadUrl.addOnSuccessListener(OnSuccessListener {
                            val imageUrl = it.toString()
                            val uId = auth.uid.toString()
                            val userData=UserModel(uId,userEmail,userName,imageUrl)

                            database.getReference().child("USERS")
                                .child(uId)
                                .setValue(userData)
                                .addOnSuccessListener(OnSuccessListener {
                                    progressBar.visibility = View.GONE
                                    val intent=Intent(this, HomeActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                })
                        })
                    }
                    else
                        progressBar.visibility = View.GONE
                        createAccountBtn.isEnabled = true
                        createAccountBtn.isClickable = true
                        createAccountBtn.alpha = 1f
                })
            }
        })

    }

    private fun validateUser():Boolean {
        if(username.length()==0){
            username.setError("Enter Your name here!")
            return false
        }
        if(imageUri ==null){
            Toast.makeText(this,"please choose your profile picture ",Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && requestCode==13)
        {
            imageUri = data?.data
            userImage.setImageURI(imageUri)
        }
    }
}