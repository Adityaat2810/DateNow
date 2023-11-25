package com.example.date_now.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.date_now.R
import com.example.date_now.activity.chatActivity
import com.example.date_now.model.UserModel

class UserAdapter(val context: Context, val userList:ArrayList<UserModel>):
    RecyclerView.Adapter<UserAdapter.userViewHolder>()


{

    class userViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val textName=itemView.findViewById<TextView>(R.id.userKaNaam)
        val textpic = itemView.findViewById<ImageView>(R.id.userImageView)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): userViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.user_layout,parent,false)
        return userViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: userViewHolder, position: Int) {


        val currentUser=userList[position]


        Glide.with(context)
            .load(currentUser.image)
            .placeholder(R.drawable.profile)
            .error(R.drawable.profile)
            .centerCrop()
            .into(holder.textpic)


        holder.textName.text = currentUser.name



        holder.itemView.setOnClickListener{
            val intent = Intent(context, chatActivity::class.java)

            intent.putExtra("name",currentUser.name)
            intent.putExtra("uid",currentUser.uid)
            intent.putExtra("image",currentUser.image)
            context.startActivity(intent)

        }
    }

}