package com.example.shoppingcorner

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingcorner.databinding.ActivityMainBinding
import com.example.shoppingcorner.models.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.toObject


class MainActivity : AppCompatActivity(), HomeRvAdapter.OnProductClicked {
    lateinit var RV: RecyclerView
    lateinit var adapter: HomeRvAdapter
    lateinit var binding: ActivityMainBinding
    lateinit var list: MutableList<Product>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFullScreen()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initActionBar()
        initButtons()
        initRV()

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

    private fun initButtons() {
        binding.btnMyProductsMain.setOnClickListener {
            startActivity(Intent(this, MyProductsActivity::class.java))
        }
        binding.btnMyOrdersMain.setOnClickListener {
            startActivity(Intent(this, MyOrdersActivity::class.java))
        }
        binding.btnMyProfileMain.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
            prefs.edit().apply {
                putBoolean("loggedIn", false)
            }.apply()
            val intent = Intent(this, SplashActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            this.startActivity(intent)
        }
    }

    private fun initRV() {
        RV = binding.homeRV
        RV.layoutManager = LinearLayoutManager(this)
        adapter = HomeRvAdapter(this)
        RV.adapter = adapter
        list = mutableListOf()
        adapter.data = list
        EventChange()

    }

    private fun EventChange() {
        val db = FirebaseFirestore.getInstance()
        db.collection("products2")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Toast.makeText(baseContext, "${error.toString()}", Toast.LENGTH_LONG).show()
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {

                            if(!FirebaseAuth.getInstance().currentUser!!.uid.equals(dc.document["uid"])){
                                list.add(dc.document.toObject(Product::class.java))

                            }
                        } else if (dc.type == DocumentChange.Type.MODIFIED) {
                            list.clear()
                            db.collection("products2").get().addOnSuccessListener {
                                for (item in it) {
                                    if (!FirebaseAuth.getInstance().currentUser!!.uid.equals(item["uid"]))
                                        list.add(item.toObject(Product::class.java))
                                    adapter.notifyDataSetChanged()
                                }
                            }
                        }
                    }

                    adapter.notifyDataSetChanged()
                }
            })


    }


    override fun onProductClicked(product: Product) {
        val intent = Intent(this, ProductViewActivity::class.java)
        intent.putExtra("productTitle", product.title)
        intent.putExtra("productDesc", product.description)
        intent.putExtra("productPrice", product.price.toString())
        intent.putExtra("productAddress", product.address)
        intent.putExtra("productMobileNumber", product.mobileNumber.toString())
        intent.putExtra("seller", product.seller)
        intent.putExtra("uid", product.uid)
        intent.putExtra("from", "main")
        intent.putExtra("productQuantity", product.quantity.toString())
        intent.putExtra("productId", "${product.id}")
        startActivity(intent)
    }



}