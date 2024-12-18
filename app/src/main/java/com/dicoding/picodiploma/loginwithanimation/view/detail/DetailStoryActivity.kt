package com.dicoding.picodiploma.loginwithanimation.view.detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.data.Result
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityDetailStoryBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory


class DetailStoryActivity : AppCompatActivity() {
    private val viewModel by viewModels<DetailStoryViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var _binding: ActivityDetailStoryBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val idUser = intent.getStringExtra(EXTRA_ID)

        viewModel.getSession().observe(this) { user ->

            viewModel.getDetailStories(user.token ,idUser.toString()).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Error -> {
                            binding?.progressBar?.visibility = View.GONE
                            Toast.makeText(
                                this,
                                "Terjadi kesalahan" + result.error,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        Result.Loading -> {
                            binding?.progressBar?.visibility = View.VISIBLE
                        }
                        is Result.Success -> {
                            binding?.progressBar?.visibility = View.GONE
                            binding?.root?.let {
                                binding?.ivDetailPhoto?.let { it1 ->
                                    Glide.with(it.context)
                                        .load(result.data.story?.photoUrl)
                                        .into(it1)
                                }
                            }
                            binding?.tvTitle?.text = result.data.story?.name.toString()
                            binding?.tvDescription?.text = result.data.story?.description.toString()
                        }

                    }
                }
            }
        }



    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }

    companion object{
        const val EXTRA_ID = "extra_id"
    }
}