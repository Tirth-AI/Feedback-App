package com.example.feedbackdo


import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.example.feedbackdo.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mAuth: FirebaseAuth
    private lateinit var selectedValue: String

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        val user = mAuth.currentUser

        if (user != null) {
            binding.name.text = "Hello, ${user.displayName}"
        } else
            binding.name.text = "Hello, User"

        val services = resources.getStringArray(R.array.service_list)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_menu_item, services)
        binding.autoCompleteText.setAdapter(arrayAdapter)


        binding.autoCompleteText.setOnItemClickListener { _, _, position, _ ->
            selectedValue = arrayAdapter.getItem(position)!!
            if (selectedValue != null) {
                binding.btnNextPage.visibility = View.VISIBLE
            }
            Toast.makeText(this, "Selected: $selectedValue", Toast.LENGTH_SHORT).show()
        }

        binding.btnNextPage.setOnClickListener {
            val intent = Intent(this@MainActivity, VideoUploadActivity::class.java)
            intent.putExtra("category_name", selectedValue)
            startActivity(intent)
        }

        binding.logoutButton.setOnClickListener {
            signOutAndStartSignInActivity()
        }
    }


    private fun signOutAndStartSignInActivity() {
        mAuth.signOut()
        mGoogleSignInClient.signOut().addOnCompleteListener(this) {
            val intent = Intent(this@MainActivity, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}

