package com.example.shoppingcorner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.*
import com.example.shoppingcorner.models.Order
import com.example.shoppingcorner.models.Product
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.toObject

class ProductViewActivity : AppCompatActivity() {
    lateinit var title: String
    lateinit var desc: String
    lateinit var price: String
    lateinit var address: String
    lateinit var mobileNumber: String
    lateinit var seller: String
    lateinit var uid: String
    var orderQuantity: Int = 0
    lateinit var from: String
    lateinit var quantityEt: EditText
    lateinit var productId: String
    lateinit var productQuantity : String


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        initFullScreen()
        setContentView(R.layout.activity_product_view)
        initActionBar()
        intent = getIntent()
        title = intent.getStringExtra("productTitle").toString()
        desc = intent.getStringExtra("productDesc").toString()
        price = intent.getStringExtra("productPrice").toString()
        address = intent.getStringExtra("productAddress").toString()
        mobileNumber = intent.getStringExtra("productMobileNumber").toString()
        seller = intent.getStringExtra("seller").toString()
        uid = intent.getStringExtra("uid").toString()
        from = intent.getStringExtra("from").toString()
        productId = intent.getStringExtra("productId").toString()
        productQuantity = intent.getStringExtra("productQuantity").toString()


        findViewById<TextView>(R.id.tv_title_product_view).setText(title)
        findViewById<TextView>(R.id.tv_desc_product_view).setText(desc)
        findViewById<TextView>(R.id.tv_price_product_view).setText("Price: " + price + "$")
        findViewById<TextView>(R.id.tv_address_product_view).setText("Address: " + address)
        findViewById<TextView>(R.id.tv_mobileNumber_product_view).setText("Mobile Number: " + mobileNumber)
        findViewById<TextView>(R.id.tv_quantity_product_view).setText("Available quantity: " + productQuantity)
        findViewById<TextView>(R.id.tv_seller_product_view).setText("Seller: "+seller)
        quantityEt = findViewById<EditText>(R.id.et_quantity_product_view)


        findViewById<FloatingActionButton>(R.id.btn_delete_product_view).setOnClickListener{

            val user = FirebaseAuth.getInstance().currentUser
            val db = FirebaseFirestore.getInstance()
            db.collection("orders")
                .addSnapshotListener(object : EventListener<QuerySnapshot> {

                    override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                        if (error != null) {
                            Toast.makeText(baseContext, "${error.toString()}", Toast.LENGTH_LONG).show()
                            return
                        }
                        for (dc: DocumentChange in value?.documentChanges!!) {
                            if (dc.type == DocumentChange.Type.ADDED) {


                                        if(dc.document.toObject(Order::class.java).productID.equals(productId)){
                                            FirebaseFirestore.getInstance().collection("orders").document(dc.document.toObject(Order::class.java).orderId).delete()
                                        }

                            }



                        }

                    }
                }
                )



            FirebaseFirestore.getInstance().collection("products2").document(productId).delete()
            val intent = Intent(this, SplashActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            this.startActivity(intent)
        }


        if (from.equals("myProducts")) {

            viewEditView()

        } else {
            findViewById<TextView>(R.id.et_title_product_view).visibility = View.INVISIBLE
            findViewById<EditText>(R.id.et_description_product_view).visibility = View.INVISIBLE
            findViewById<EditText>(R.id.et_address_product_view).visibility = View.INVISIBLE
            findViewById<EditText>(R.id.et_mobile_number_product_view).visibility = View.INVISIBLE
            findViewById<EditText>(R.id.et_price_product_view).visibility = View.INVISIBLE
            findViewById<FloatingActionButton>(R.id.btn_delete_product_view).visibility=View.INVISIBLE

        }

        findViewById<Button>(R.id.btn_product_view).setOnClickListener {
            onButtonClicked()
        }
    }

    private fun viewEditView() {
        findViewById<Button>(R.id.btn_product_view).setText("Update")

        findViewById<TextView>(R.id.tv_title_product_view).visibility = View.INVISIBLE
        findViewById<TextView>(R.id.tv_desc_product_view).visibility = View.INVISIBLE
        findViewById<TextView>(R.id.tv_address_product_view).visibility = View.INVISIBLE
        findViewById<TextView>(R.id.tv_mobileNumber_product_view).visibility = View.INVISIBLE
        findViewById<TextView>(R.id.tv_price_product_view).visibility = View.INVISIBLE
        findViewById<TextView>(R.id.tv_quantity_product_view).visibility = View.INVISIBLE

        findViewById<EditText>(R.id.et_title_product_view).visibility = View.VISIBLE
        findViewById<EditText>(R.id.et_description_product_view).visibility = View.VISIBLE
        findViewById<EditText>(R.id.et_address_product_view).visibility = View.VISIBLE
        findViewById<EditText>(R.id.et_mobile_number_product_view).visibility = View.VISIBLE
        findViewById<EditText>(R.id.et_price_product_view).visibility = View.VISIBLE

        findViewById<EditText>(R.id.et_title_product_view).setText(title)
        findViewById<EditText>(R.id.et_description_product_view).setText(desc)
        findViewById<EditText>(R.id.et_address_product_view).setText(address)
        findViewById<EditText>(R.id.et_mobile_number_product_view).setText(mobileNumber)
        findViewById<EditText>(R.id.et_price_product_view).setText(price)
        findViewById<EditText>(R.id.et_quantity_product_view).setText(productQuantity)


    }


    fun onButtonClicked() {
        if (from.equals("main")) {


            if (quantityEt.text.toString()
                    .isNotEmpty()
            ) {
                orderQuantity =
                    Integer.parseInt(quantityEt.text.toString())
            } else {
                Toast.makeText(baseContext,"Enter a valid quantity",Toast.LENGTH_SHORT).show()
            }
            if ((orderQuantity <= productQuantity.toLong()) &&  quantityEt.text.toString()
                    .isNotEmpty()) {
                val doc = FirebaseFirestore.getInstance().collection("orders").document()
                var order = Order(
                    title,
                    FirebaseAuth.getInstance().currentUser!!.uid,
                    productId,
                    doc.id,
                    orderQuantity,
                    false
                )
                doc
                    .set(
                        order

                    ).addOnCompleteListener {
                        if (it.isSuccessful) {
                            FirebaseFirestore.getInstance().collection("products2").document(productId).update(
                                "quantity",productQuantity.toLong()-orderQuantity.toLong()
                            )

                            Toast.makeText(this, "Order placed", Toast.LENGTH_LONG).show()
                            finish()
                        } else {
                            Toast.makeText(this, "error", Toast.LENGTH_LONG).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Enter a valid Quantity", Toast.LENGTH_SHORT)
                    .show()
            }

        } else {
            if (findViewById<TextView>(R.id.et_title_product_view).text.isNotEmpty() &&
                findViewById<EditText>(R.id.et_description_product_view).text.isNotEmpty() &&
                findViewById<EditText>(R.id.et_address_product_view).text.isNotEmpty() &&
                findViewById<EditText>(R.id.et_mobile_number_product_view).text.isNotEmpty() &&
                findViewById<EditText>(R.id.et_price_product_view).text.isNotEmpty() &&
                findViewById<EditText>(R.id.et_quantity_product_view).text.isNotEmpty()&&
                !(findViewById<TextView>(R.id.et_title_product_view).text.equals(title) ||
                        findViewById<EditText>(R.id.et_description_product_view).text.equals(desc) ||
                        findViewById<EditText>(R.id.et_address_product_view).text.equals(address) ||
                        findViewById<EditText>(R.id.et_mobile_number_product_view).text.equals(
                            mobileNumber
                        ) ||
                        findViewById<EditText>(R.id.et_quantity_product_view).text.equals(productQuantity)||
                        findViewById<EditText>(R.id.et_price_product_view).text.equals(price) )

            ) {
                finish()
                FirebaseFirestore.getInstance().collection("products2").document("$productId")
                    .update(
                        mapOf(
                            "uid" to "$uid",
                            "seller" to "${seller}",
                            "address" to "${findViewById<EditText>(R.id.et_address_product_view).text}",
                            "mobileNumber" to ("${findViewById<EditText>(R.id.et_mobile_number_product_view).text}".toLong()),
                            "id" to "$productId",
                            "title" to "${findViewById<TextView>(R.id.et_title_product_view).text}",
                            "description" to "${findViewById<EditText>(R.id.et_description_product_view).text}",
                            "price" to ("${findViewById<EditText>(R.id.et_price_product_view).text}".toLong()),
                            "quantity" to ("${findViewById<EditText>(R.id.et_quantity_product_view).text}".toLong())
                        )

                    )
            }


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
}