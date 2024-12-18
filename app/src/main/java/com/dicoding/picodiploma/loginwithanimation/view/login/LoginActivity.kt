package com.dicoding.picodiploma.loginwithanimation.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.Result
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityLoginBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.main.MainActivity
import com.dicoding.picodiploma.loginwithanimation.view.signup.SignupActivity
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding

    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        playAnimation()

        binding?.btnSignin?.setOnClickListener {
            val email = binding?.edEmail?.text.toString()
            val pwd = binding?.edPassword?.text.toString()
            viewModel.postUserLogin(email, pwd).observe(this) { result ->
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
                            viewModel.saveSession(UserModel(email, result.data.loginResult?.token.toString()))
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }

        }



        binding?.btnSignup?.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
            finish()
        }


    }

    private fun playAnimation() {
        val welcomeText: TextView = findViewById(R.id.tv_welcome)
        val hintText: TextView = findViewById(R.id.textView3)
        val inputEmail: TextInputLayout = findViewById(R.id.input_email)
        val inputPassword: TextInputLayout = findViewById(R.id.input_password)
        val btnSignIn: Button = findViewById(R.id.btn_signin)

        val slideUpWelcome = ObjectAnimator.ofFloat(welcomeText, "translationY", 500f, 0f)
        val slideUpHint = ObjectAnimator.ofFloat(hintText, "translationY", 500f, 0f)
        val slideUpEmail = ObjectAnimator.ofFloat(inputEmail, "translationY", 500f, 0f)
        val slideUpPassword = ObjectAnimator.ofFloat(inputPassword, "translationY", 500f, 0f)
        val slideUpButton = ObjectAnimator.ofFloat(btnSignIn, "translationY", 500f, 0f)

        slideUpWelcome.duration = 1000
        slideUpHint.duration = 1000
        slideUpEmail.duration = 1000
        slideUpPassword.duration = 1000
        slideUpButton.duration = 1000

        slideUpWelcome.start()
        slideUpHint.start()
        slideUpEmail.start()
        slideUpPassword.start()
        slideUpButton.start()
    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}