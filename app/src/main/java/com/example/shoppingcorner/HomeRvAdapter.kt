package com.example.shoppingcorner

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingcorner.models.Product

class HomeRvAdapter(listener:OnProductClicked): RecyclerView.Adapter<HomeRvAdapter.ViewHolder>() {
    var listener = listener
    var data = listOf<Product>()

    inner class ViewHolder (itemView: View):RecyclerView.ViewHolder(itemView),View.OnClickListener{
        val title :TextView = itemView.findViewById(R.id.tv_title_order_card)
        val price :TextView = itemView.findViewById(R.id.tv_quantity_order_card)
        val address:TextView = itemView.findViewById(R.id.tv_delivred_order_card)
        val card:LinearLayout=itemView.findViewById(R.id.myCardView)
        init {
            itemView.setOnClickListener(this)


        }

        override fun onClick(p0: View?) {
            listener!!.onProductClicked(data.get(adapterPosition))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vh = LayoutInflater.from(parent.context).inflate(R.layout.product_card,parent,false)
        return ViewHolder(vh)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text=data.get(position).title
        holder.price.text=data.get(position).price.toString()+"$"
        holder.address.text=data.get(position).address
        if(position%2==0){
            holder.card.setBackgroundColor(Color.parseColor("#D85329"))
            holder.title.setTextColor(Color.parseColor("#FFFFFF"))
            holder.price.setTextColor(Color.parseColor("#FFFFFF"))
            holder.address.setTextColor(Color.parseColor("#FFFFFF"))
        }
    }

    override fun getItemCount() = data.size

    interface OnProductClicked{
       fun onProductClicked(product: Product)
    }


}