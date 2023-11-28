package com.example.date_now

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.example.date_now.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.hdodenhof.circleimageview.CircleImageView

class MainActivity : AppCompatActivity() {

    private lateinit var toogle:ActionBarDrawerToggle
    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //supportActionBar?.hide()



        binding =ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController=findNavController(R.id.fragment)
        NavigationUI.setupWithNavController(binding.bottomNavigationView,
        navController)


        val drwewrLayout: DrawerLayout = findViewById(R.id.DrawerLayout)
        val navView: NavigationView = findViewById(R.id.navView)

        val navHeaderView = navView.getHeaderView(0)
        val myImage = navHeaderView.findViewById<ImageView>(R.id.myImage)
        val userName = navHeaderView.findViewById<TextView>(R.id.user_name)
        val userEmail = navHeaderView.findViewById<TextView>(R.id.user_email)

        //val myImage = findViewById<CircleImageView>(R.id.myImage)
        val imageRef = FirebaseDatabase.getInstance().getReference("users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("image")

        val nameRef = FirebaseDatabase.getInstance().getReference("users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("name")

        val emailRef = FirebaseDatabase.getInstance().getReference("users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("email")

        toogle = ActionBarDrawerToggle(this,drwewrLayout,R.string.open,R.string.close)

        drwewrLayout.addDrawerListener(toogle)
        toogle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.navHome -> Toast.makeText(applicationContext, "you ", Toast.LENGTH_SHORT).show()
            }
            true

        }

        imageRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val imageUri = snapshot.value?.toString()

                if (!imageUri.isNullOrBlank()) {
                    Log.d("MainActivity", "Image URL: $imageUri") // Add this line for logging

                    Glide.with(this@MainActivity)
                        .load(imageUri)
                        .into(myImage)
                } else {
                    Log.e("MainActivity", "Image URL is null or blank")
                    // Handle the case where the imageUri is null or blank
                    // You might want to show a placeholder image or handle it in another way
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error
                Toast.makeText(this@MainActivity, "Failed to load image", Toast.LENGTH_SHORT).show()
            }
        })

        nameRef.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val naam = snapshot.value?.toString()
                userName.setText(naam)

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

       emailRef.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val emaaal = snapshot.value?.toString()
                userEmail.setText(emaaal)

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })








    }


}