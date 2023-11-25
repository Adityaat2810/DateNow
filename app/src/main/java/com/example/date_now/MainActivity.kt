package com.example.date_now

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.date_now.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()


        binding =ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController=findNavController(R.id.fragment)
        NavigationUI.setupWithNavController(binding.bottomNavigationView,
        navController)


    }
}