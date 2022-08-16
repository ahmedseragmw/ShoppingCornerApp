package com.example.shoppingcorner.repository

import android.widget.Toast
import com.example.shoppingcorner.models.Order
import com.example.shoppingcorner.models.Product
import com.google.firebase.firestore.*
/*
class Repository {

    companion object{
        var INSTANCE : Repository? = null
        fun getInstance():Repository{
            var instance = INSTANCE
            if(instance == null)
            {
                instance = Repository()
                INSTANCE=instance
            }
            return instance
        }


    }

    init{
        getAllOrders()
        getAllProducts()
    }

    var allProducts = mutableListOf<Product>()
    var allOrders = mutableListOf<Order>()

    fun getAllProducts(){
        FirebaseFirestore.getInstance().collection("products2")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val users = document.toObject(Product::class.java)
                    allProducts.add(users)
                }
            }
            .addOnFailureListener { exception ->
            }

    }

    fun getAllOrders(){
        val db = FirebaseFirestore.getInstance()
        db.collection("orders")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {

                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            allOrders.add(dc.document.toObject(Order::class.java))
                        }
                    }

                }
            })

    }

    fun getProduct(productID:String):Product{
        getAllOrders()
        getAllProducts()
         var itemm:Product=allProducts.get(0)
        for(item : Product in allProducts)
        {
            if(item.id.equals(productID))
                itemm=item

        }
        return itemm
    }



}*/