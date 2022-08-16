package com.example.shoppingcorner

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingcorner.models.Order
import com.example.shoppingcorner.models.Product

class OrdersAdapter(listener:OnOrderClicked): RecyclerView.Adapter<OrdersAdapter.ViewHolder>() {

    var listener = listener
    var data = listOf<Order>()

    inner class ViewHolder (itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val title : TextView = itemView.findViewById(R.id.tv_title_order_card)
        val quantity : TextView = itemView.findViewById(R.id.tv_quantity_order_card)
        val delivered: TextView = itemView.findViewById(R.id.tv_delivred_order_card)
        val card: LinearLayout =itemView.findViewById(R.id.myCardView)
        init {
            itemView.setOnClickListener(this)


        }

        override fun onClick(p0: View?) {
            listener!!.onOrderClicked(data.get(adapterPosition))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vh = LayoutInflater.from(parent.context).inflate(R.layout.product_card,parent,false)
        return ViewHolder(vh)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text=data.get(position).productName
        holder.quantity.text=data.get(position).quantity.toString()
        if (data.get(position).delivered){
            holder.delivered.text="Delivered"
        }
        else {
            holder.delivered.text="Not delivered"
        }
        if(position%2==0){
            holder.card.setBackgroundColor(Color.parseColor("#D85329"))
            holder.title.setTextColor(Color.parseColor("#FFFFFF"))
            holder.delivered.setTextColor(Color.parseColor("#FFFFFF"))
            holder.quantity.setTextColor(Color.parseColor("#FFFFFF"))
        }
    }

    override fun getItemCount() = data.size

    interface OnOrderClicked{
        fun onOrderClicked(order: Order)
    }


}