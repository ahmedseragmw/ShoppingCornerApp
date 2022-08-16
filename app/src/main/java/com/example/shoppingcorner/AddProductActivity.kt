package com.example.shoppingcorner

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.shoppingcorner.databinding.ActivityAddProductBinding
import com.example.shoppingcorner.models.Product
import com.example.shoppingcorner.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

class AddProductActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddProductBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFullScreen()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_product)
        initActionBar()


        binding.btnSaveProductAddProduct.setOnClickListener {
            saveProduct()
        }


    }


    private fun initActionBar() {
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
    }

    private fun initFullScreen() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }


    private fun saveProduct() {
        val title = binding.etProductTitle.text.toString()
        val description = binding.etProductDescription.text.toString()
        var price: Long = 0
        if (binding.etProductPrice.text.toString().isNotEmpty())
            price = (binding.etProductPrice.text.toString().toLong())
        else
            price = 1
        val address = binding.etAddressAddProduct.text.toString()
        var mobileNumber: Long = 0
        if (binding.etProductPrice.text.toString().isNotEmpty())
            mobileNumber = (binding.etMobileNumberAddProduct.text.toString().toLong())
        else
            mobileNumber = 1
        val user = FirebaseAuth.getInstance().currentUser


        if (title.isNotEmpty() && description.isNotEmpty() && binding.etProductPrice.text.isNotEmpty() && description.isNotEmpty() &&
            binding.etAddressAddProduct.text.isNotEmpty() && binding.etMobileNumberAddProduct.text.isNotEmpty() &&
            binding.etQuntityAddProduct.text.isNotEmpty()
        ) {
            val newEntry = FirebaseFirestore.getInstance().collection("products2").document()
            val productId = newEntry.id




            FirebaseFirestore.getInstance().collection("users")
                .document(FirebaseAuth.getInstance().currentUser!!.uid).get().addOnSuccessListener {

                    newEntry.set(

                        Product(
                            user!!.uid,
                            binding.etQuntityAddProduct.text.toString().toLong(),
                            address, mobileNumber, productId, title, description, price,
                            it
                                .toObject(User::class.java)!!
                                .firstName + " " +
                                    it.toObject(User::class.java)!!
                                        .lastName
                        )

                    ).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(this, "product added", Toast.LENGTH_LONG).show()
                            finish()
                        } else {
                            Toast.makeText(this, "error", Toast.LENGTH_LONG).show()

                        }
                    }
                }

        } else {
            Toast.makeText(this, "Please fill all entries", Toast.LENGTH_LONG).show()
        }

    }
}