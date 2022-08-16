package com.example.shoppingcorner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingcorner.databinding.ActivityMyOrdersBinding
import com.example.shoppingcorner.models.Order
import com.example.shoppingcorner.models.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.toObject

class MyOrdersActivity : AppCompatActivity(), OrdersAdapter.OnOrderClicked {
    lateinit var RV: RecyclerView
    lateinit var adapter: OrdersAdapter
    lateinit var binding: ActivityMyOrdersBinding
    lateinit var list: MutableList<Order>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFullScreen()
        setContentView(R.layout.activity_my_orders)


        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_orders)

        initActionBar()
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

    private fun initRV() {
        RV = binding.homeRV
        RV.layoutManager = LinearLayoutManager(this)
        adapter = OrdersAdapter(this)
        RV.adapter = adapter
        list = mutableListOf()
        adapter.data = list
        EventChange()

    }

    private fun EventChange() {
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
                            if (dc.document.toObject(Order::class.java).uid.equals(user!!.uid))
                                list.add(dc.document.toObject(Order::class.java))

                        }
                        else if(dc.type == DocumentChange.Type.MODIFIED){
                            for(item in list){
                                if (item.orderId.equals(dc.document.toObject(Order::class.java).orderId)){
                                    item.delivered=true
                                }
                            }
                        }


                    }

                    adapter.notifyDataSetChanged()
                }
            })


    }




    override fun onOrderClicked(order: Order) {

        val intent = Intent(this, OrderViewActivity::class.java)

        val db=FirebaseFirestore.getInstance().collection("products2").whereEqualTo("id","${order.productID}")
            .get().addOnSuccessListener {
                for(doc in it){
                    var myProduct: Product = doc.toObject(Product::class.java)

                    intent.putExtra(
                        "orderQuantity",
                        order.quantity.toString()
                    )

                    intent.putExtra(
                        "orderId",
                        order.orderId
                    )
                    intent.putExtra(
                        "orderDelivered",
                        order.delivered.toString()
                    )

                    intent.putExtra(
                        "productDesc",
                        myProduct.description
                    )
                    intent.putExtra(
                        "productTitle",
                        myProduct.title
                    )
                    intent.putExtra(
                        "productPrice",
                        myProduct.price.toString()
                    )
                    intent.putExtra(
                        "productAddress",
                        myProduct.address
                    )
                    intent.putExtra(
                        "productMobileNumber",
                        myProduct.mobileNumber.toString()

                    )
                    intent.putExtra(
                        "seller",
                        myProduct.seller
                    )

                    intent.putExtra(
                        "productQuantity",
                        myProduct.quantity.toString()
                    )

                    intent.putExtra(
                        "productId",
                        myProduct.id.toString()
                    )

                    intent.putExtra(
                        "productId",
                        myProduct   .id
                    )
                    intent.putExtra("delivered", order.delivered.toString())
                    intent.putExtra(
                        "orderId",
                        order.orderId.toString()
                    )
                    startActivity(intent)


                }
            }.addOnFailureListener{
                Toast.makeText(baseContext,"ERROR",Toast.LENGTH_SHORT).show()
            }







    }
}
