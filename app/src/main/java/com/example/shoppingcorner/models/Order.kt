package com.example.shoppingcorner.models

class Order(
    var productName:String="",
    var uid:String="",
    var productID:String="",
    var orderId:String="",
    var quantity:Int=0,
    var delivered:Boolean=false
)