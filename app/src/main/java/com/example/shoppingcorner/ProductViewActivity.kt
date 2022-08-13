package com.example.shoppingcorner

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage

class ProductViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFullScreen()
        setContentView(R.layout.activity_product_view)
        initActionBar()
        val intent = getIntent()
        val title = intent.getStringExtra("productTitle")
        val desc = intent.getStringExtra("productDesc")
        val price = intent.getStringExtra("productPrice")
        val address = intent.getStringExtra("productAddress")
        val mobileNumber = intent.getStringExtra("productMobileNumber")
        val seller = intent.getStringExtra("seller")
        findViewById<TextView>(R.id.tv_title_product_view).setText(title)
        findViewById<TextView>(R.id.tv_desc_product_view).setText(desc)
        findViewById<TextView>(R.id.tv_price_product_view).setText("Price: "+price+"$")
        findViewById<TextView>(R.id.tv_address_product_view).setText("Address: "+address)
        findViewById<TextView>(R.id.tv_mobileNumber_product_view).setText("Mobile Number: "+mobileNumber)
        findViewById<TextView>(R.id.tv_seller_product_view).setText("Seller: "+seller)

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
}