package com.example.shoppingcorner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.shoppingcorner.databinding.ActivityRegisterBinding
import com.example.shoppingcorner.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class RegisterActivity : AppCompatActivity() {
    lateinit var binding:ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding = DataBindingUtil.setContentView(this,R.layout.activity_register)
        supportActionBar!!.hide()


        binding.btnSubmitRegister.setOnClickListener {
            registerUser()
        }


    }

    private fun registerUser() {
        if (verifyEntries()) {
            if(binding.etPasswordRegister.text.toString().equals(binding.etConfirmPasswordRegister.text.toString())){
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    binding.etEmailRegister.text.toString(), binding.etPasswordRegister.text.toString()
                ).addOnFailureListener {
                    Toast.makeText(baseContext,"${it.toString()}",Toast.LENGTH_LONG).show()
                }

                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val fireStoreUser: FirebaseUser = task.result.user!!
                            val user = User(
                                fireStoreUser.uid,
                                binding.etFirstNameRegister.text.toString(),
                                binding.etLastNameRegister.text.toString(),
                                binding.etEmailRegister.text.toString()
                            )

                            FirebaseFirestore.getInstance().collection("users").document(user.id).set(
                                user, SetOptions.merge()
                            ).addOnFailureListener {
                                Toast.makeText(this, "${it.toString()}", Toast.LENGTH_LONG).show()
                            }.addOnCompleteListener {
                                if (it.isSuccessful) {
                                    Toast.makeText(this, "Account created", Toast.LENGTH_SHORT).show()
                                    finish()
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(
                                        this, "Something 22222 went wrong",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(
                                this, "Something went wrong",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
            else {
                Toast.makeText(baseContext,"Validate your password",Toast.LENGTH_LONG).show()
            }

        } else {
            Toast.makeText(
                this, "Please fill all entries",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun verifyEntries():Boolean{
        return binding.etFirstNameRegister.text.isNotEmpty() &&
                binding.etLastNameRegister.text.isNotEmpty()&&
                binding.etEmailRegister.text.isNotEmpty()&&
                binding.etPasswordRegister.text.isNotEmpty()&&
                binding.etConfirmPasswordRegister.text.isNotEmpty()&&
                binding.checkBoxPolicy.isChecked
    }


}

