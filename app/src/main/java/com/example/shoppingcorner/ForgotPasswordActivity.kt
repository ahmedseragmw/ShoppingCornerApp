package com.example.shoppingcorner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.shoppingcorner.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {
    lateinit var binding : ActivityForgotPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding = DataBindingUtil.setContentView(this,R.layout.activity_forgot_password)
        if(supportActionBar!=null)
            supportActionBar!!.hide()
        binding.btnResetPassword.setOnClickListener {
            resetPassword()
        }
    }

    private fun resetPassword() {
        if(binding.etEmailForgotPassword.text.isNotEmpty()){
            FirebaseAuth.getInstance().sendPasswordResetEmail(binding.etEmailForgotPassword.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(this,"Account createed", Toast.LENGTH_SHORT).show()
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }

        else
        {
            Toast.makeText(baseContext,"Please enter a valid email",Toast.LENGTH_LONG).show()
        }

    }
}