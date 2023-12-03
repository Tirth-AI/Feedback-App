package com.example.feedbackdo

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.feedbackdo.databinding.ActivityQuestionnaireBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class QuestionnaireActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuestionnaireBinding
    private var questionList = ArrayList<Question>()
    private lateinit var categoryName: String
    private lateinit var videoUrl: String
    private var customProgressDialog: Dialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionnaireBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showCustomDialog()
        categoryName = intent.getStringExtra("category_name")!!
        videoUrl = intent.getStringExtra("video_url")!!
        val databaseRef = FirebaseDatabase.getInstance().reference.child("Questions/$categoryName")

        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("QuestionnaireActivity", "$snapshot")
                if (snapshot.exists()) {
                    for (questionSnapshot in snapshot.children) {
                        Log.d("QuestionnaireActivity", "For loop : $questionSnapshot")
                        questionList.add(Question(questionSnapshot.value.toString(), 0F))
                    }
                    Log.d("QuestionnaireActivity", "List: $questionList")
                    val questionAdapter = QuestionAdapter(questionList)
                    binding.rvQuestionnaire.adapter = questionAdapter

                    cancelCustomDialog()
                } else {
                    Log.d("QuestionnaireActivity", "It doesn't exist")
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("QuestionnaireActivity", "Failed to read: $error")
                cancelCustomDialog()
            }
        })

        binding.btnSubmit.setOnClickListener {
            showCustomDialog()
            uploadFeedbackToFirebase()
            val intent = Intent(this@QuestionnaireActivity, FormSubmittedActivty::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun uploadFeedbackToFirebase() {
        binding.btnSubmit.visibility = View.GONE
        val currentUser = Firebase.auth.currentUser
        val path = System.currentTimeMillis().toString() + categoryName
        var databaseUploadRef =
            FirebaseDatabase.getInstance().reference.child("Feedback/").child(currentUser!!.uid)
                .child(path).child("Questionnaire_Rating")
        databaseUploadRef.setValue(questionList)

        databaseUploadRef =
            FirebaseDatabase.getInstance().reference.child("Feedback/").child(currentUser!!.uid)
                .child(path).child("Feedback_Video_URL")
        databaseUploadRef.setValue(videoUrl)
        cancelCustomDialog()
    }

    private fun showCustomDialog() {
        customProgressDialog = Dialog(this@QuestionnaireActivity)
        customProgressDialog?.setContentView(R.layout.custom_progress_dialog)
        customProgressDialog?.show()
    }

    private fun cancelCustomDialog() {
        if (customProgressDialog != null) {
            customProgressDialog?.dismiss()
            customProgressDialog = null
        }
    }
}