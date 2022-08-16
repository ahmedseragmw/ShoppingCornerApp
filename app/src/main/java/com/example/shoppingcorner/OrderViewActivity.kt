package com.example.shoppingcorner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.shoppingcorner.models.Order
import com.example.shoppingcorner.models.Product
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.*

class OrderViewActivity : AppCompatActivity() {

    lateinit var title: String
    lateinit var desc: String
    lateinit var price: String
    lateinit var address: String
    lateinit var mobileNumber: String
    lateinit var seller: String
    lateinit var quantity:String
    lateinit var orderId:String
    lateinit var productID:String


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        initFullScreen()
        setContentView(R.layout.activity_order_view)
        initActionBar()



        intent = getIntent()
        title = intent.getStringExtra("productTitle").toString()
        desc = intent.getStringExtra("productDesc").toString()
        price = intent.getStringExtra("productPrice").toString()
        address = intent.getStringExtra("productAddress").toString()
        mobileNumber = intent.getStringExtra("productMobileNumber").toString()
        seller = intent.getStringExtra("seller").toString()
        quantity = intent.getStringExtra("orderQuantity").toString()
        orderId = intent.getStringExtra("orderId").toString()
        productID = intent.getStringExtra("productId").toString()
        val delivered=intent.getStringExtra("delivered")
        findViewById<TextView>(R.id.tv_title_product_view).setText(title)
        findViewById<TextView>(R.id.tv_desc_product_view).setText(desc)
        findViewById<TextView>(R.id.tv_price_product_view).setText("Price: " + price + "$")
        findViewById<TextView>(R.id.tv_address_product_view).setText("Address: " + address)
        findViewById<TextView>(R.id.tv_mobileNumber_product_view).setText("Mobile Number: " + mobileNumber)
        findViewById<TextView>(R.id.tv_quantity_product_view).setText("Seller: " + seller)
        findViewById<TextView>(R.id.tv_quantity_order_view).setText("Quantity: "+quantity.toString())



        val deliveredTv=findViewById<TextView>(R.id.tv_delivered_order_view)
        val orderId = intent.getStringExtra("orderId")

        if(delivered.equals("true")){

            findViewById<Button>(R.id.btn_delivered_order_view).isEnabled=false
            findViewById<TextView>(R.id.tv_delivered_order_view).visibility= View.INVISIBLE
        }
        else{
            deliveredTv.setText("NOT Delivered Yet")
        }


        findViewById<Button>(R.id.btn_delivered_order_view).setOnClickListener {
            if(delivered.equals("false")){
                deliveredTv.visibility =View.INVISIBLE
                FirebaseFirestore.getInstance().collection("orders").document("${intent.getStringExtra("orderId")}")
                    .update("delivered",true)
                findViewById<Button>(R.id.btn_delivered_order_view).isEnabled=false
            }
        }

        findViewById<FloatingActionButton>(R.id.btn_delete_order_view).setOnClickListener{


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


                                if(dc.document.toObject(Product::class.java).id.equals(productID)&&intent.getStringExtra("orderDelivered").toString().equals("false")){
                                    FirebaseFirestore.getInstance().collection("products2")
                                        .document(dc.document.toObject(Product::class.java).id)
                                        .update("quantity",quantity.toLong()+(intent.getStringExtra("productQuantity"))!!.toLong())

                                }

                            }



                        }

                    }
                }
                )

                FirebaseFirestore.getInstance().collection("orders").document(orderId!!).delete()


            val intent = Intent(this, SplashActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            this.startActivity(intent)
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