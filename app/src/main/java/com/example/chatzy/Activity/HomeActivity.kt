package com.example.chatzy.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.example.chatzy.Adapters.chatListAdapter
import com.example.chatzy.Models.UserModel
import com.example.chatzy.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    val dataset:ArrayList<UserModel> = ArrayList()
    var database = FirebaseDatabase.getInstance()
    var auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // set custumize toolbar
        setSupportActionBar(homeToolbar)

        var userProfile : String? =null
        var userEmail :String? =null
        var userName :String? = null


        var usersAdapter = chatListAdapter(dataset,this)
        recyclerviewChatList.adapter=usersAdapter

        database.getReference().child("USERS").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                dataset.clear()
                for(snapshot1:DataSnapshot in snapshot.children)
                {
                    var user: UserModel? = snapshot1.getValue(UserModel::class.java)
                    if(user?.userid.toString() != auth.uid.toString())
                        dataset.add(user!!)
                    else{
                        userProfile = user?.userimageurl
                        userEmail = user?.useremail
                        userName  = user?.username
                        Glide.with(this@HomeActivity)
                            .load(userProfile)
                            .into(userImage)
                    }
                }
                usersAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        userImage.setOnClickListener(View.OnClickListener {
            val intent = Intent(this,SettingsActivity::class.java).apply {
                putExtra("userEmail",userEmail)
                putExtra("userProfile",userProfile)
                putExtra("userName",userName)
            }
            startActivity(intent)
        })
    }
}