package com.example.date_now.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.date_now.R
import com.example.date_now.model.Message
import com.google.firebase.auth.FirebaseAuth

class messageAdapter (val context: Context,val messageList:ArrayList<Message>):
RecyclerView.Adapter<RecyclerView.ViewHolder>(){


    val ITEM_RECIEVE=1;
    val ITEM_SENT=2


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType ==1){
            //inflate recieve
            val view: View = LayoutInflater.from(context).
                    inflate(R.layout.recieve,parent ,false)
            return RecieveViewHolder(view)

        }else{
            val view:View= LayoutInflater.from(context).inflate(R.layout.send,parent,false)
            return SentViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messageList[position]
        if(holder.javaClass == SentViewHolder::class.java){
            // do the stuff for sent view Holder
            val viewHolder = holder as SentViewHolder
            holder.sentMessage.text = currentMessage.message

        }else{
            //do the stuff for receive view holder
            val viewHolder= holder as RecieveViewHolder
            holder.recievedMessage.text=currentMessage.message

        }

    }


    override fun getItemViewType(position: Int): Int {
        val currentMessage= messageList[position]

        if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)){
            return  ITEM_SENT

        }
        else{
            return ITEM_RECIEVE
        }
    }





    class SentViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val sentMessage = itemView.findViewById<TextView>(R.id.sentMessage)



    }
    class RecieveViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val recievedMessage = itemView.findViewById<TextView>(R.id.recievedMessage)

    }
}