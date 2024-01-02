package com.example.date_now.ui

import android.content.ContentValues.TAG
import android.os.Bundle
import android.support.v4.os.IResultReceiver2.Default
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import com.example.date_now.R
import com.example.date_now.adapter.DatingAdapter
import com.example.date_now.databinding.FragmentDatingBinding
import com.example.date_now.databinding.ItemUserLayoutBinding
import com.example.date_now.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction


class DatingFragment : Fragment() {

    private lateinit var binding: FragmentDatingBinding
    private lateinit var manager:CardStackLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentDatingBinding.inflate(layoutInflater)

        getData()






        return binding.root
    }

    private fun inita() {
        manager = CardStackLayoutManager(requireContext(),object:CardStackListener{
            override fun onCardDragging(direction: Direction?, ratio: Float) {

            }

            override fun onCardSwiped(direction: Direction?) {
                if(manager.topPosition==list.size){
                    Toast.makeText(requireContext(), "This is last card", Toast.LENGTH_SHORT).show()
                }

                if(direction ==Direction.Right){
                    Toast.makeText(requireContext(), "you liked her ", Toast.LENGTH_SHORT).show()
                    val likedUserMail = list[manager.topPosition - 1].email
                    addToFavourate(likedUserMail)

                }

                if(direction ==Direction.Left){

                    Toast.makeText(requireContext(), "you reject her ", Toast.LENGTH_SHORT).show()


                }

            }

            override fun onCardRewound() {

            }

            override fun onCardCanceled() {

            }

            override fun onCardAppeared(view: View?, position: Int) {

            }

            override fun onCardDisappeared(view: View?, position: Int) {

            }

        })

        manager.setVisibleCount(3)
        manager.setTranslationInterval(0.6f)
        manager.setScaleInterval(0.8f)
        manager.setMaxDegree(20.0f)
        manager.setDirections(Direction.HORIZONTAL)
    }

    private fun addToFavourate(likedUserMail: String?) {
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid

        if (currentUserUid != null && likedUserMail != null) {
            val favoritesRef = FirebaseDatabase.getInstance().getReference("users")
                .child(currentUserUid)
                .child("favorite")

            val likedUserKey = likedUserMail.replace('.', '_')

            favoritesRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.hasChild(likedUserKey)) {
                        favoritesRef.child(likedUserKey).setValue(true)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Log.d(TAG, "User added to favorites successfully")
                                } else {
                                    Log.e(TAG, "Failed to add user to favorites", task.exception)
                                }
                            }
                    } else {
                        Log.d(TAG, "User is already in favorites")
                        Toast.makeText(requireContext(), "user already in favorites", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "Error checking favorites", error.toException())
                    Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Log.e(TAG, "Current user or likedUserMail is null")
            Toast.makeText(requireContext(), "Current user or likedUserMail is null", Toast.LENGTH_SHORT).show()
        }
    }


    private lateinit var  list:ArrayList<UserModel>



    private fun getData() {
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid

        if (currentUserUid != null) {
            val userReference = FirebaseDatabase.getInstance().getReference("users")
                .child(currentUserUid)

            userReference.child("gender").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Retrieve the user's gender from the dataSnapshot
                    val gender = dataSnapshot.value.toString()

                    // Now, you have the gender, proceed to fetch and filter the data
                    fetchAndFilterUsers(gender)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle errors if any
                    Log.e("TAG", "Error getting user gender", databaseError.toException())
                }
            })
        } else {
            Log.e(TAG, "Current user is null")
            Toast.makeText(requireContext(), "Current user is null", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchAndFilterUsers(userGender: String) {
        FirebaseDatabase.getInstance().getReference("users")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // Filter the list based on gender
                    val filteredList = ArrayList<UserModel>()

                    if (snapshot.exists()) {
                        for (data in snapshot.children) {
                            val model = data.getValue(UserModel::class.java)
                            if (model?.gender?.toLowerCase() != userGender.toLowerCase()) {
                                filteredList.add(model!!)
                            }
                        }

                        // Shuffle the filtered list to generate a random sequence every time
                        filteredList.shuffle()

                        // Initialize card stack and set up recycler view here
                        inita()

                        binding.cardStackView.layoutManager = manager
                        binding.cardStackView.itemAnimator = DefaultItemAnimator()
                        binding.cardStackView.adapter = DatingAdapter(requireContext(), filteredList)
                    } else {
                        Toast.makeText(requireContext(), "something went wrong", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
                }
            })
    }






}