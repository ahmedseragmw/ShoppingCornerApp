package com.example.shoppingcorner.models

import android.net.Uri
import com.google.android.gms.tasks.Task

class Product(
    var uid: String = "",
    var quantity: Long = 0,
    var address: String = "",
    var mobileNumber: Long = 0,
    var id: String = "",
    var title: String = "",
    var description: String = "",
    var price: Long = 0,
    var seller: String = ""
)
