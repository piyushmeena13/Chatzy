package com.example.chatzy.Activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.example.chatzy.Adapters.conversationAdapter
import com.example.chatzy.Models.MessageModel
import com.example.chatzy.R
import com.example.chatzy.encryprion
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_conversation.*
import java.util.*

class ConversationActivity : AppCompatActivity() {

    val auth = FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation)

        // create a object of our encryption.kt class use this object to call our function from encryption class
        var aesEncryption:encryprion = encryprion()

        supportActionBar?.hide()
        val username = intent.getStringExtra("userName")
        val userImage = intent.getStringExtra("userProfile")
        val receiverid = intent.getStringExtra("receiverid")
        val senderid = auth.uid

        // update data of reciver on toolbar
        userNameChatActivity.setText(username)
        Glide.with(this)
            .load(userImage)
            .into(profileChatActivity)

        backArrowBtn.setOnClickListener(View.OnClickListener {
            super.onBackPressed()
        })

        //chat room
        val senderroom = senderid + receiverid
        val receiverroom = receiverid + senderid

        //set adapter
        var messageDataset:ArrayList<MessageModel> = ArrayList()
        var msgAdapter = conversationAdapter(messageDataset,this)
        recyclerViewConversationActivity.adapter =msgAdapter

        //showing chats in recyclerview or  read msg from firebase(read database)
        database.getReference().child("CHATS").child(senderroom)
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageDataset.clear()
                    for(snapshot1:DataSnapshot in snapshot.children)
                    {
                        var message: MessageModel? = snapshot1.getValue(MessageModel::class.java)
                        messageDataset.add(message!!)
                    }
                    msgAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        //send msg chats on firebase (write database)
        sendMsgBtn.setOnClickListener(View.OnClickListener {
            if(msgBoxET.length()!=0)
            {
                val message = aesEncryption.encrypt(msgBoxET.text.toString())
                msgBoxET.text.clear()

                val date= Date()
                var messagemodel:MessageModel = MessageModel(senderid.toString(),message, date.time)

                database.getReference().child("CHATS")
                    .child(senderroom).push()
                    .setValue(messagemodel).addOnSuccessListener(OnSuccessListener {

                        database.getReference().child("CHATS")
                            .child(receiverroom).push()
                            .setValue(messagemodel).addOnSuccessListener(OnSuccessListener {

                            })

                    })

            }
        })
    }



}