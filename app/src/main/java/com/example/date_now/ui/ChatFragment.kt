package com.example.date_now.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.date_now.R
import com.example.date_now.adapter.UserAdapter
import com.example.date_now.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatFragment : Fragment() {

    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userList: ArrayList<UserModel>
    private lateinit var adapter: UserAdapter
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_chat, container, false)

        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().getReference()
        userList = ArrayList()
        adapter = UserAdapter(requireContext(), userList)

        userRecyclerView = rootView.findViewById(R.id.userList)
        userRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        userRecyclerView.adapter = adapter

        val myUid = mAuth.currentUser?.uid
        val currentUserEmail = FirebaseDatabase.getInstance().getReference("users").
        child(myUid!!).child("email").toString()


        mDbRef.child("users").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()

                for (postSnapshot in snapshot.children) {
                    val thisUser = postSnapshot.getValue(UserModel::class.java)
                    val thisUserEmail = thisUser?.email.toString()
                    val thisUserKey = thisUserEmail.replace('.', '_')


                    // Check if the user is in my favorites
                    mDbRef.child("users").child(myUid!!).child("favorite")
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(favoriteSnapshot: DataSnapshot) {
                                if (favoriteSnapshot.hasChild(thisUserKey)) {
                                    // Check if I am in their favorites
                                    mDbRef.child("users").child(thisUser!!.uid!!)
                                        .child("favorite")
                                        .addListenerForSingleValueEvent(object : ValueEventListener {
                                            override fun onDataChange(theirFavoriteSnapshot: DataSnapshot) {
                                                val myEmail = FirebaseAuth.getInstance().currentUser?.email.toString()

                                                val myKey = myEmail.replace('.', '_')
                                                if (theirFavoriteSnapshot.hasChild(myKey)) {
                                                    // Both users are in each other's favorites, so it's a match
                                                    userList.add(thisUser!!)
                                                    adapter.notifyDataSetChanged()
                                                }
                                            }

                                            override fun onCancelled(error: DatabaseError) {
                                                Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
                                            }
                                        })
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()

                            }
                        })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()

            }

        })

        return rootView
    }
}
