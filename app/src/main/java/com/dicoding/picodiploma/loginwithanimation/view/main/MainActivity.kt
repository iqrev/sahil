package com.dicoding.picodiploma.loginwithanimation.view.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.Result
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityMainBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.addphoto.AddPhotoActivity
import com.dicoding.picodiploma.loginwithanimation.view.login.LoginActivity


class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val mainAdapter = MainAdapter()

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                binding?.topAppBar?.visibility = View.VISIBLE
                binding?.btnStory?.visibility = View.VISIBLE
                viewModel.getAllStories(user.token).observe(this) { result ->
                    if (result != null) {
                        when (result){
                            Result.Loading -> {
                                binding?.progressBar?.visibility = View.VISIBLE
                            }
                            is Result.Error -> {
                                binding?.progressBar?.visibility = View.GONE
                                Toast.makeText(
                                    this,
                                    "Terjadi kesalahan" + result.error,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            is Result.Success -> {
                                binding?.progressBar?.visibility = View.GONE
                                mainAdapter.submitList(result.data.listStory)
                            }

                        }
                    }
                }

            }
        }

        binding?.topAppBar?.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.btn_logout -> {
                    viewModel.logout()
                    finish()
                    true
                }
                else -> false
            }
        }

        binding?.rvItemStory?.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = mainAdapter
        }

        binding?.btnStory?.setOnClickListener {
            startActivity(Intent(this, AddPhotoActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}