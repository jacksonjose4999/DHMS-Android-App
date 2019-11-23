package com.example.cmms

import com.google.firebase.Timestamp
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.cart_items_layout_toadd.view.*
import kotlinx.android.synthetic.main.item_names.view.*
import kotlinx.android.synthetic.main.passbook_orders_history_layout.view.*
import kotlinx.android.synthetic.main.receipt_items.view.*
import kotlinx.android.synthetic.main.stall_names.view.*

class StallNameItem(var name: String) : Item<ViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.stall_names
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.stall_heading_label.text = name
    }

}

class ItemName(var name: String,var description: String, var price: String, var isVeg: Boolean, var stallName: String) : Item<ViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.item_names
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.item_text_view.text = name
        viewHolder.itemView.item_price_view.text = "Rs. "+price
    }

}

class CartItemsToOrder(var items: CartItems) : Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.dish_name_cart.text = items.qty.toString()+" x "+items.itemName.name
        var priceOfItems = items.itemName.price.toFloat()*items.qty
        viewHolder.itemView.cost_cart.text = "Rs. "+priceOfItems
    }

    override fun getLayout(): Int {
        return R.layout.cart_items_layout_toadd
    }
}

class PassbookHistory(var name: String, var price: String, var qty: Long) : Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.dish_name_history.text = qty.toString()+" x "+name
        var priceOfItems = price.toFloat()*qty
        viewHolder.itemView.cost_cart_history.text = "Rs. "+priceOfItems
       // viewHolder.itemView.timestamp_history.text = timestamp.toString()
    }

    override fun getLayout(): Int {
        return R.layout.passbook_orders_history_layout
    }
}

class ReceiptItems(var quantity: Long, var name: String) : Item<ViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.receipt_items
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.item_and_quantity.text = quantity.toString()+" x "+name
    }
}