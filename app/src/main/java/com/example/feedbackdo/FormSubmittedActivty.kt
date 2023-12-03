package com.example.feedbackdo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class FormSubmittedActivty : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_submitted_activty)

        findViewById<Button>(R.id.btn_goToHomePage).setOnClickListener {
            val intent = Intent(this@FormSubmittedActivty, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}