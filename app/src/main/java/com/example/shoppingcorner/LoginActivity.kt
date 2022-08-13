package com.example.shoppingcorner

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import com.example.shoppingcorner.databinding.ActivityLoginBinding
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFullScreen()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        initActionBar()
        initDestinations()
    }

    private fun initDestinations() {
        binding.tvCreateAcc.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.btnLoginLogin.setOnClickListener {
            loginUser()
        }
        binding.tvForgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }


    private fun initActionBar() {
        if(supportActionBar!=null){
            supportActionBar!!.hide()
        }
    }

    private fun initFullScreen() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    private fun loginUser() {
        if(binding.etEmailLogin.text.isNotEmpty()&&binding.etPasswordLogin.text.isNotEmpty()){
            FirebaseAuth.getInstance().signInWithEmailAndPassword(
                binding.etEmailLogin.text.toString(),
                binding.etPasswordLogin.text.toString()
            )
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
                        prefs.edit().apply {
                            putString("email",binding.etEmailLogin.text.toString())
                            putString("password",binding.etPasswordLogin.text.toString())
                            putBoolean("loggedIn",true)
                        }.apply()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(
                            baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

        }

        else {
            Toast.makeText(
                baseContext, "Please fill all entries",
                Toast.LENGTH_SHORT
            ).show()
        }



    }

}


