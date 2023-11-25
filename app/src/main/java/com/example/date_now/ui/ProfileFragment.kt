package com.example.date_now.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.date_now.R
import com.example.date_now.activity.EditProfileActivity
import com.example.date_now.auth.login
import com.example.date_now.databinding.FragmentProfileBinding
import com.example.date_now.model.UserModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.dialog.MaterialDialogs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.getValue


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        binding = FragmentProfileBinding.inflate(layoutInflater)

     FirebaseDatabase.getInstance().getReference("users")
         .child(FirebaseAuth.getInstance().currentUser!!.uid!!).get()
         .addOnSuccessListener {
             if(it.exists()){
                 val data  = it.getValue(UserModel::class.java)
                 binding.userName.setText(data!!.name.toString())
                 binding.userCity.setText(data!!.city.toString())
                 binding.userEmail.setText(data!!.email.toString())
                 binding.userNumber.setText(data!!.number.toString())

                 Glide.with(this).load(data.image)
                     .placeholder(R.drawable.profile).into(binding.userPic)
             }
         }


     binding.logout.setOnClickListener {
         FirebaseAuth.getInstance().signOut()
         startActivity(Intent(requireContext(),login::class.java))
         requireActivity().finish()

     }

     binding.editProfile.setOnClickListener {
         startActivity(Intent(requireContext() , EditProfileActivity::class.java))

     }

        return binding.root
    }


}