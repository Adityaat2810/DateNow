package com.example.date_now.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.date_now.MainActivity
import com.example.date_now.R
import com.example.date_now.auth.login
import com.google.firebase.auth.FirebaseAuth

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val user = FirebaseAuth.getInstance().currentUser
        supportActionBar?.hide()

        Handler(Looper.getMainLooper()).postDelayed({
            if(user ==null){
                startActivity(Intent(this,login::class.java))
                finish()
            }else{
                startActivity(Intent(this,MainActivity::class.java))
                finish()

            }
        },1200)

    }
}