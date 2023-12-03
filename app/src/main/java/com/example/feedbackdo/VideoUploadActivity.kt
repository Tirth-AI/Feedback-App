package com.example.feedbackdo

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.feedbackdo.databinding.ActivityVideoUploadBinding
import com.google.firebase.Firebase
import com.google.firebase.storage.storage

class VideoUploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVideoUploadBinding
    private var isPlaying = true
    private lateinit var videoUri: Uri
    private lateinit var categoryName : String
    private var customProgressDialog: Dialog? = null
    private lateinit var uploadedVideoUrl : String

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        categoryName = intent.getStringExtra("category_name")!!
        binding.tvCategory.text = "${binding.tvCategory.text}  $categoryName"

        binding.btnSelectVideo.setOnClickListener {
            selectVideo()
        }
        
        binding.videoView.setOnClickListener {
            togglePlayPause()
        }

        binding.btnChangeVideo.setOnClickListener{
            selectVideo()
        }

        binding.btnUploadvideo.setOnClickListener {
            showCustomDialog()
            binding.btnUploadvideo.visibility = View.GONE
            binding.btnChangeVideo.visibility = View.GONE
            uploadVideoToFirebase()
        }

        binding.btnNextPage.setOnClickListener {
            val intent = Intent(this@VideoUploadActivity, QuestionnaireActivity::class.java)
            intent.putExtra("category_name", categoryName)
            intent.putExtra("video_url", uploadedVideoUrl)
            startActivity(intent)
            finish()
        }

    }

    private fun selectVideo() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "video/*"
        videoLauncher.launch(intent)
    }

    private val videoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == Activity.RESULT_OK)
        {
            binding.btnSelectVideo.visibility = View.GONE
            binding.btnUploadvideo.visibility = View.VISIBLE
            binding.btnChangeVideo.visibility = View.VISIBLE
            videoUri = it.data!!.data!!

            binding.videoView.setVideoURI(videoUri)
            binding.videoView.visibility = View.VISIBLE
            binding.videoView.start()

            Log.d("VideoUploadActivity", "Selected Video : $videoUri")
            Toast.makeText(this, "Video Loaded", Toast.LENGTH_SHORT).show()
        }
    }

    private fun togglePlayPause() {
        if(isPlaying) {
            binding.videoView.pause()
        }else {
            binding.videoView.start()
        }
        isPlaying = !isPlaying
    }

    private fun uploadVideoToFirebase() {
        val fileName = "${System.currentTimeMillis()}_$categoryName.mp4"
        val ref = Firebase.storage.reference.child("videos").child("$fileName")

        ref.putFile(videoUri)
            .addOnSuccessListener {
                binding.btnNextPage.visibility = View.VISIBLE
                cancelCustomDialog()
                ref.downloadUrl.addOnSuccessListener {
                    uploadedVideoUrl = it.toString()
                }
                Toast.makeText(this@VideoUploadActivity, "Video uploaded", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                cancelCustomDialog()
                binding.btnUploadvideo.visibility = View.VISIBLE
                Toast.makeText(this@VideoUploadActivity, "Error occurred while uploading", Toast.LENGTH_SHORT).show()
                Log.d("VideoUploadActivity", "Uploading Error : $it")

            }
    }

    private fun showCustomDialog() {
        customProgressDialog = Dialog(this@VideoUploadActivity)
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