package com.example.cmms

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.xwray.groupie.Group
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.cart_fragment.*
import kotlinx.android.synthetic.main.menu_fragment.*
import java.lang.Exception
import android.view.Window.FEATURE_NO_TITLE




open class CartFragment : Fragment(){
    var adapter = GroupAdapter<ViewHolder>()
    var arrayList = OrderFragment.cartitemsList
    var totalAmountCart = 0.0
    var closingBalance = 0.0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var db = FirebaseFirestore.getInstance()
        val v =  inflater.inflate(R.layout.cart_fragment, null)
        var recyclerView = v.findViewById<RecyclerView>(R.id.recycler_view_cart_items)
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView!!.adapter = adapter

        var emptyCartButton = v.findViewById<Button>(R.id.empty_cart)
        emptyCartButton.setOnClickListener {
            OrderFragment.cartitemsList.clear()
            /*adapter.notifyDataSetChanged()
            val ft = fragmentManager!!.beginTransaction()
            ft.detach(this).attach(this).commit()*/
            refreshRecyclerView()
        }

        for( i in 0 until arrayList.size){
            adapter.add(CartItemsToOrder(arrayList[i]))
            totalAmountCart += arrayList[i].qty*(arrayList[i].itemName.price.toDouble())
        }

        var totalamountText = v.findViewById<TextView>(R.id.total_amount_cart)
        totalamountText.text = "Rs.  "+totalAmountCart

         closingBalance = (MainActivity.availableBalance - totalAmountCart)

        v.findViewById<TextView>(R.id.closing_balance_cart).text = "Closing Balance : Rs."+"%.2f".format(closingBalance)
        if(closingBalance<0){
            v.findViewById<TextView>(R.id.waring_lower_balance_message).text = "You do not have sufficient balance"
            var textViewPlaceOrder = v.findViewById<Button>(R.id.placeorder_cart)
            textViewPlaceOrder.background = resources.getDrawable(R.drawable.low_balance_button_view)
        }

        adapter.setOnItemClickListener { item, view ->
            try {
                item as CartItemsToOrder
                val editItemsInCart = EditItemInCart(item.items.itemName.name, item.items.itemName.description,
                    item.items.itemName.price, item.items.itemName,adapter)
                Log.wtf("hjhjhjkjj","$item.items.itemName.name")
                editItemsInCart.show(fragmentManager, "AddToCartTag")
                adapter.notifyDataSetChanged()
            }
            catch ( e : Exception){

            }
        }

        var placeOrderCartButton = v.findViewById<Button>(R.id.placeorder_cart)
        placeOrderCartButton.setOnClickListener {
            if (closingBalance<0){
                return@setOnClickListener
            }
            for(i in 0 until OrderFragment.cartitemsList.size) {



                val id = db.collection("Users/${MainActivity.netID}/Orders").document().id
                db.collection("Users/${MainActivity.netID}/Orders").document(id)
                    .set(OrderItem(OrderFragment.cartitemsList[i].itemName.name, OrderFragment.cartitemsList[i].itemName.description,
                        OrderFragment.cartitemsList[i].qty, OrderFragment.cartitemsList[i].itemName.stallName, "${MainActivity.netID}",
                        (OrderFragment.cartitemsList[i].itemName.price.toDouble()).toString()))

                db.collection("Orders/${OrderFragment.cartitemsList[i].itemName.stallName}/Orders").document(id).set(OrderItem(OrderFragment.cartitemsList[i].itemName.name,
                    OrderFragment.cartitemsList[i].itemName.description, OrderFragment.cartitemsList[i].qty, OrderFragment.cartitemsList[i].itemName.stallName, "${MainActivity.netID}",
                     (OrderFragment.cartitemsList[i].itemName.price.toDouble()).toString()))
                MainActivity.availableBalance = closingBalance

                Log.wtf("addingitems", "$id sdfsd")
            }
            val docData = hashMapOf(
                "name" to MainActivity.nameUser,
                "dining hall" to 1,
                "roll number" to MainActivity.rollNoUser,
                "balance" to closingBalance
            )

            var doc = db.collection("Users/").document(MainActivity.netID)
                .set(docData)



            OrderFragment.cartitemsList.clear()
            /*adapter.notifyDataSetChanged()
            val ft = fragmentManager!!.beginTransaction()
            ft.detach(this).attach(this).commit()*/
            refreshRecyclerView()

        }

        return v
    }

    fun refreshRecyclerView(){
        var f = CartFragment()
        fragmentManager!!.beginTransaction().replace(R.id.frame_container,f!!).commit()
    }



}



data class OrderItem(
    val name: String? = null,
    val description: String? = null,
    val quantity: Long? = null,
    val stall: String? = null,
    val userID: String? = null,
    val price: String? = null,
    val timeStamp: com.google.firebase.Timestamp = com.google.firebase.Timestamp.now()
)

class EditItemInCart(val name : String, val description: String , val price: String, val itemName: ItemName, val adapter: GroupAdapter<ViewHolder>) : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.add_to_cart_layout, container)

        dialog.window.setBackgroundDrawable( ColorDrawable(Color.TRANSPARENT))

        this.dialog.setTitle("Edit Cart")
        val nameTextView = rootView.findViewById<TextView>(R.id.name_item_add_to_cart)
        val descriptionTextView = rootView.findViewById<TextView>(R.id.description_item_add_to_cart)
        val priceTextView = rootView.findViewById<TextView>(R.id.price_item_add_to_cart)

        nameTextView.text = name
        descriptionTextView.text = description
        priceTextView.text = "Rs."+price

        var minusButton = rootView.findViewById<Button>(R.id.minus_add)
        var plusButton = rootView.findViewById<Button>(R.id.plus_add)
        var qtyView = rootView.findViewById<TextView>(R.id.item_count_add_to_cart)
        var priceText = rootView.findViewById<TextView>(R.id.price_item_add_to_cart)

        for (i in 0 until OrderFragment.cartitemsList.size){
            if (OrderFragment.cartitemsList[i].itemName.name.equals(itemName.name)){
                qtyView.text = OrderFragment.cartitemsList[i].qty.toString()
            }
        }

        plusButton.setOnClickListener {
            var num = qtyView.text.toString().toInt()
            if (num == 5){
                return@setOnClickListener
            }
            num++
            qtyView.text = num.toString()
            var currPrice = price.toFloat()*num
            priceText.text = "Rs."+currPrice
        }

        minusButton.setOnClickListener {
            var num = qtyView.text.toString().toInt()
            if (num == 0){
                return@setOnClickListener
            }
            num--
            qtyView.text = num.toString()
            var currPrice = price.toFloat()*num
            priceText.text = "Rs."+currPrice
        }

        var addButton = rootView.findViewById<Button>(R.id.add_to_cart_button)
        addButton.text = "Ok"
        addButton.setOnClickListener {
            var qtt = qtyView.text.toString().toInt()
            for (i in 0 until OrderFragment.cartitemsList.size){
                if (OrderFragment.cartitemsList[i].itemName.name == itemName.name){
                    OrderFragment.cartitemsList[i].qty = qtt.toLong()
                    if (qtt == 0){
                        OrderFragment.cartitemsList.removeAt(i)
                        break
                    }
                }
            }
            for(i in 0 until OrderFragment.cartitemsList.size)
                Log.wtf("oopps","${OrderFragment.cartitemsList[i].qty} ${OrderFragment.cartitemsList[i].itemName.name}")
            adapter.notifyDataSetChanged()
            var f = CartFragment()
            fragmentManager!!.beginTransaction().replace(R.id.frame_container,f!!).commit()
            this.dismiss()

        }

        return rootView
    }
}
