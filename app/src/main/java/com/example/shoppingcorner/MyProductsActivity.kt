package com.example.shoppingcorner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingcorner.databinding.ActivityMainBinding
import com.example.shoppingcorner.databinding.ActivityMyProductsBinding
import com.example.shoppingcorner.models.Order
import com.example.shoppingcorner.models.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.toObject

class MyProductsActivity : AppCompatActivity(), HomeRvAdapter.OnProductClicked {
    lateinit var RV: RecyclerView
    lateinit var adapter: HomeRvAdapter
    lateinit var binding: ActivityMyProductsBinding
    lateinit var list: MutableList<Product>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initFullScreen()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_products)

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
        binding.btnAddProduct.setOnClickListener {
            startActivity(Intent(this, AddProductActivity::class.java))
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
        val user = FirebaseAuth.getInstance().currentUser
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
                            if (dc.document.toObject(Product::class.java).uid.equals(user!!.uid)) {
                                list.add(dc.document.toObject(Product::class.java))

                            }
                        } else if (dc.type == DocumentChange.Type.MODIFIED) {
                            for (item in list) {
                                if (item.id.equals(dc.document.toObject(Product::class.java).id)) {
                                    list[(list.indexOf(item))] =
                                        dc.document.toObject(Product::class.java)
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
        intent.putExtra("from", "myProducts")
        intent.putExtra("productId", "${product.id}")
        intent.putExtra("productQuantity", product.quantity.toString())


        startActivity(intent)
    }
}