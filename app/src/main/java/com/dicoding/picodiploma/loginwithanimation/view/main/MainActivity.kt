package com.dicoding.picodiploma.loginwithanimation.view.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityMainBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.addphoto.AddPhotoActivity
import com.dicoding.picodiploma.loginwithanimation.view.login.LoginActivity
import com.dicoding.picodiploma.loginwithanimation.view.maps.MapsActivity


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
        binding?.rvItemStory?.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = mainAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    mainAdapter.retry()
                }
            )
        }

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                binding?.topAppBar?.visibility = View.VISIBLE
                binding?.btnStory?.visibility = View.VISIBLE
                binding?.btnMaps?.visibility = View.VISIBLE
                viewModel.getAllStories(user.token).observe(this) {
                    mainAdapter.submitData(lifecycle, it)
                    binding?.progressBar?.visibility = View.GONE
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



        binding?.btnStory?.setOnClickListener {
            startActivity(Intent(this, AddPhotoActivity::class.java))
        }

        binding?.btnMaps?.setOnClickListener {
            startActivity(Intent(this, MapsActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}