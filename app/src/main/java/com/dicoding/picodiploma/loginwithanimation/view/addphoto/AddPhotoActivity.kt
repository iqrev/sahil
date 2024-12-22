package com.dicoding.picodiploma.loginwithanimation.view.addphoto

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.Result
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityAddPhotoBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.getImageUri
import com.dicoding.picodiploma.loginwithanimation.view.main.MainActivity
import com.dicoding.picodiploma.loginwithanimation.view.reduceFileImage
import com.dicoding.picodiploma.loginwithanimation.view.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class AddPhotoActivity : AppCompatActivity() {
    private var _binding: ActivityAddPhotoBinding? = null
    private val binding get() = _binding

    private var currentImageUri: Uri? = null

    private val viewModel by viewModels<AddPhotoViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddPhotoBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.previewImageView?.setOnClickListener {
            startGallery()
        }

        binding?.cameraButton?.setOnClickListener {
            startCamera()
        }

        binding?.uploadButton?.setOnClickListener {
            uploadImage()
        }
    }

    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            val description = binding?.etDesc?.text.toString()
            binding?.progressBar?.visibility = View.VISIBLE
            val requestBody = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )
            viewModel.getSession().observe(this) { user ->
                viewModel.uploadImage(user.token, multipartBody, requestBody)
                    .observe(this) { result ->
                        if (result != null) {
                            when (result) {
                                is Result.Error -> {
                                    binding?.progressBar?.visibility = View.GONE
                                    Toast.makeText(
                                        this,
                                        getString(R.string.error) + ": " + result.error,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                                Result.Loading -> {
                                    binding?.progressBar?.visibility = View.VISIBLE
                                }

                                is Result.Success -> {
                                    binding?.progressBar?.visibility = View.GONE
                                    Toast.makeText(
                                        this,
                                        result.data.message.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    val intent = Intent(this, MainActivity::class.java)
                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                }

                            }
                        }
                    }
            }
        } ?: Toast.makeText(this, R.string.empty_image_warning, Toast.LENGTH_SHORT).show()
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri!!)
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        } else {
            currentImageUri = null
            Toast.makeText(this, getString(R.string.gambar_tidak_diambil), Toast.LENGTH_SHORT)
                .show()
        }
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Toast.makeText(
                this,
                getString(R.string.tidak_ada_gambar_yang_di_pilih), Toast.LENGTH_SHORT
            ).show()

        }
    }


    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding?.previewImageView?.setImageURI(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}