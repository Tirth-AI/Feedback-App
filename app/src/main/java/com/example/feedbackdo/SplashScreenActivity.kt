package com.example.feedbackdo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var intent : Intent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val currentUser = Firebase.auth.currentUser

        intent = if(currentUser != null) {
            Intent(this@SplashScreenActivity, MainActivity::class.java)
        } else {
            Intent(this@SplashScreenActivity, SignInActivity::class.java)
        }

        Handler().postDelayed({
            startActivity(intent)
            finish()
        },2000)

    }
}