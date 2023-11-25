package com.example.date_now.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.date_now.databinding.ItemUserLayoutBinding
import com.example.date_now.model.UserModel

class DatingAdapter(val context:Context,
                 val list:ArrayList<UserModel>):RecyclerView.Adapter<DatingAdapter.DatingViewHolder> (){


    inner class DatingViewHolder(val binding:ItemUserLayoutBinding)
        :RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DatingViewHolder {

        return DatingViewHolder(ItemUserLayoutBinding.inflate(LayoutInflater.from(context),
        parent,false))

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: DatingViewHolder, position: Int) {

        holder.binding.textView4.text = list[position].name
        holder.binding.textView3.text = list[position].email

        Glide.with(context).load(list[position].image).into(holder.binding.userImage)

    }

}