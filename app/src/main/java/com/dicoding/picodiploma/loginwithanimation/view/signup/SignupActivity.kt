package com.dicoding.picodiploma.loginwithanimation.view.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.Result
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivitySignupBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.login.LoginActivity
import com.dicoding.picodiploma.loginwithanimation.view.main.MainActivity

class SignupActivity : AppCompatActivity() {
    private var _binding: ActivitySignupBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding?.root)



        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val viewModel: SignUpViewModel by viewModels {
            factory
        }

        binding?.btnSignup?.setOnClickListener {
            val name = binding?.etName?.text.toString()
            val email = binding?.etEmail?.text.toString()
            val pwd = binding?.etPassword?.text.toString()
            viewModel.postUserRegister(name, email, pwd).observe(this) { result ->
                if (result != null){
                    when (result){
                        is Result.Error -> {
                            binding?.progressBar?.visibility = View.GONE
                            Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                        }
                        Result.Loading -> {
                            binding?.progressBar?.visibility = View.VISIBLE
                        }
                        is Result.Success -> {
                            binding?.progressBar?.visibility = View.GONE
                            Toast.makeText(this, result.data.message.toString(), Toast.LENGTH_SHORT).show()
                            viewModel.postUserLogin(email, pwd).observe(this) { result ->
                                if (result != null){
                                    when (result){
                                        is Result.Loading -> {
                                            binding?.progressBar?.visibility = View.VISIBLE
                                        }
                                        is Result.Success -> {
                                            binding?.progressBar?.visibility = View.GONE
                                            viewModel.saveSession(UserModel(email, result.data.loginResult?.token.toString()))
                                            val intent = Intent(this, MainActivity::class.java)
                                            startActivity(intent)
                                            finish()
                                        }
                                        is Result.Error -> {
                                            binding?.progressBar?.visibility = View.GONE
                                            Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }

        binding?.btnSignin?.setOnClickListener {
            startActivity(Intent( this@SignupActivity, LoginActivity::class.java))
            finish()
        }


    }



    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}