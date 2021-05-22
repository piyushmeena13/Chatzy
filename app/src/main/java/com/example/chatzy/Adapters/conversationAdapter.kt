package com.example.chatzy.Adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.chatzy.Models.MessageModel
import com.example.chatzy.R
import com.example.chatzy.encryprion
import com.google.firebase.auth.FirebaseAuth

@RequiresApi(Build.VERSION_CODES.O)
class conversationAdapter(val messagesdata:ArrayList<MessageModel>, val context: Context):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // create a object of our encryption.kt class use this object to call our function from encryption class

    var aesEncryption: encryprion = encryprion()

    val ITEM_SENT:Int = 1
    val ITEM_RECEIVE:Int = 2

    class senderViewHolder(view: View):RecyclerView.ViewHolder(view){
        val senderMessageTv = view.findViewById<TextView>(R.id.sendMessageTv)
    }
    class receiverViewHolder(view: View):RecyclerView.ViewHolder(view){
        val receiverMessageTv = view.findViewById<TextView>(R.id.receiveMessageTv)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType ==ITEM_SENT){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_send,parent,false)
            return senderViewHolder(view)
        }
        else{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.receive_item,parent,false)
            return receiverViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var message:MessageModel = messagesdata[position]
        if(holder.javaClass ==senderViewHolder::class.java){
            var viewHolder:senderViewHolder = holder as senderViewHolder
            viewHolder.senderMessageTv.text = aesEncryption.decrypt(message.userMessages.toString())
        }else{
            var viewHolder:receiverViewHolder = holder as receiverViewHolder
            viewHolder.receiverMessageTv.text = aesEncryption.decrypt(message.userMessages.toString())
        }
    }

    override fun getItemCount(): Int {
        return  messagesdata.size
    }

    override fun getItemViewType(position: Int): Int {

        val message:MessageModel = messagesdata[position]

        if(FirebaseAuth.getInstance().uid.equals(message.senderID))
            return ITEM_SENT
        else
            return ITEM_RECEIVE
    }
}