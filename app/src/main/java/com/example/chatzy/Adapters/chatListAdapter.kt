package com.example.chatzy.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatzy.Activity.ConversationActivity
import com.example.chatzy.Models.UserModel
import com.example.chatzy.R

class chatListAdapter(val dataset:ArrayList<UserModel>, val context: Context):
    RecyclerView.Adapter<chatListAdapter.userViewHolder>() {

    class userViewHolder(view: View):RecyclerView.ViewHolder(view){
        val username = view.findViewById<TextView>(R.id.username)
        val lastMsg = view.findViewById<TextView>(R.id.lastMsg)
        val msgTime = view.findViewById<TextView>(R.id.msgTime)
        val profile = view.findViewById<ImageView>(R.id.profile)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): userViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_list,parent,false)
        return userViewHolder(view)
    }

    override fun onBindViewHolder(holder: userViewHolder, position: Int) {

        var user = dataset[position]
        holder.username.text = user.username
        holder.lastMsg.text = user.useremail
        var url = user.userimageurl
        Glide.with(context)
            .load(url)
            .into(holder.profile)

        holder.itemView.setOnClickListener(View.OnClickListener {
            val intent = Intent(context,ConversationActivity::class.java).apply {
                putExtra("userName",user.username)
                putExtra("userProfile",user.userimageurl)
                putExtra("receiverid",user.userid)
            }
            context.startActivity(intent)
        })
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}