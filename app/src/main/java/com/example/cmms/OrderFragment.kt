package com.example.cmms

import android.content.ContentValues.TAG
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cmms.OrderFragment.Companion.cartitemsList
import com.google.firebase.firestore.FirebaseFirestore
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.add_to_cart_layout.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.LocalTime


open class OrderFragment : Fragment(){
    var arrayListBuffet = ArrayList<ItemName>()
    var arrayListAla = ArrayList<ItemName>()
    var arrayListRoll = ArrayList<ItemName>()
    var arrayListBakery = ArrayList<ItemName>()

    companion object{
        var cartitemsList = ArrayList<CartItems>()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.order_fragment, null)

        var recyclerViewBuffet = view.findViewById<RecyclerView>(R.id.recycler_view_orders)
        var adapter = GroupAdapter<ViewHolder>()
        recyclerViewBuffet.layoutManager = LinearLayoutManager(this.context)
        recyclerViewBuffet!!.adapter = adapter


        adapter.setOnItemClickListener { item, view ->
            try {
                item as ItemName
                val addToCartDialog = AddToCartDialog(item.name, item.description, item.price, item)
                addToCartDialog.show(fragmentManager, "AddToCartTag")
            }
            catch ( e : Exception){

            }
        }

        var breakfastStart = LocalTime.parse("07:00")
        var breakfastEnd = LocalTime.parse("10:00")
        var lunchStart = LocalTime.parse("12:00")
        var lunchEnd = LocalTime.parse("15:00")
        var dinnerStart = LocalTime.parse("19:00")
        var dinnerEnd = LocalTime.parse("22:00")
        var snacksStart = LocalTime.parse("15:30")
        var snacksEnd = LocalTime.parse("18:00")
        var midnightMessStart = LocalTime.parse("23:00")
        var midnightMessEnd = LocalTime.parse("23:59")

        //val currentTime = Calendar.getInstance().time
        var localDateFormat = SimpleDateFormat("HH:mm:ss");
        //var time = localDateFormat.format(currentTime)
        //time = time.substring(0,5)
        //var currTime = LocalTime.parse(time)
        var currTime = LocalTime.parse("20:00")
        Log.wtf("lkjk","$currTime")

        var db = FirebaseFirestore.getInstance()


        when {
            isBetween(currTime,breakfastStart,breakfastEnd) -> {
                Log.wtf("lkjk","breakfasttime")

                db.collection("Menu/Breakfast/Buffet")
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            Log.d(TAG, "${document.id} => ${document.data}")

                            document.getString("name")?.let { ItemName(it,
                                document.getString("description")!!, document.getString("price")!!, document.getBoolean("isVeg")!!, "Buffet"
                            ) }?.let { arrayListBuffet.add(it)
                                Log.wtf("QQQQQ","Items are added")
                                if (arrayListBuffet.size == result.size()){
                                    adapter.add(StallNameItem("Buffet"))
                                    Log.wtf("QQQQQ","Size of arrayList is ${arrayListBuffet.size}")
                                    for (i in 0 until arrayListBuffet.size){
                                        adapter.add(arrayListBuffet[i])
                                    }
                                }
                            }
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d(TAG, "Error getting documents: ", exception)
                    }
                db.collection("Menu/Breakfast/Ala-Carte")
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            Log.d(TAG, "${document.id} => ${document.data}")

                            document.getString("name")?.let { ItemName(it,
                                document.getString("description")!!, document.getString("price")!!, document.getBoolean("isVeg")!!,"Ala-Carte"
                            ) }?.let { arrayListAla.add(it)

                                if (arrayListAla.size == result.size()){
                                    adapter.add(StallNameItem("Ala-Carte"))
                                    if (arrayListAla.size != 0)
                                        for (i in 0 until arrayListAla.size){
                                            adapter.add(arrayListAla[i])
                                        }
                                }
                            }
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d(TAG, "Error getting documents: ", exception)
                    }

            }

            isBetween(currTime, lunchStart, lunchEnd) -> {
                Log.wtf("lkjk","lunchstime")
                db.collection("Menu/Lunch/Buffet")
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            Log.d(TAG, "${document.id} => ${document.data}")
                            document.getString("name")?.let { ItemName(it,
                                document.getString("description")!!, document.getString("price")!!, document.getBoolean("isVeg")!!,"Buffet"
                            ) }?.let { arrayListBuffet.add(it)
                                Log.wtf("QQQQQ","Items are added")
                                if (arrayListBuffet.size == result.size()){
                                    adapter.add(StallNameItem("Buffet"))
                                    Log.wtf("QQQQQ","Size of arrayList is ${arrayListBuffet.size}")
                                    for (i in 0 until arrayListBuffet.size){
                                        adapter.add(arrayListBuffet[i])
                                    }
                                }
                            }
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d(TAG, "Error getting documents: ", exception)
                    }
                db.collection("Menu/Lunch/Ala-Carte")
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            Log.d(TAG, "${document.id} => ${document.data}")
                            document.getString("name")?.let { ItemName(it,
                                document.getString("description")!!, document.getString("price")!!, document.getBoolean("isVeg")!!,"Ala-Carte"
                            ) }?.let { arrayListAla.add(it)
                                if (arrayListAla.size == result.size()){
                                    adapter.add(StallNameItem("Ala-Carte"))
                                    if (arrayListAla.size != 0)
                                        for (i in 0 until arrayListAla.size){
                                            adapter.add(arrayListAla[i])
                                        }
                                }
                            }
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d(TAG, "Error getting documents: ", exception)
                    }

                db.collection("Menu/Lunch/Roll Maal")
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            Log.d(TAG, "${document.id} => ${document.data}")
                            document.getString("name")?.let { ItemName(it,
                                document.getString("description")!!, document.getString("price")!!, document.getBoolean("isVeg")!!,"Roll Maal"
                            ) }?.let { arrayListRoll.add(it)
                                if (arrayListRoll.size == result.size()){
                                    adapter.add(StallNameItem("Roll Mall"))
                                    if (arrayListRoll.size != 0)
                                        for (i in 0 until arrayListRoll.size){
                                            adapter.add(arrayListRoll[i])
                                        }
                                }
                            }
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d(TAG, "Error getting documents: ", exception)
                    }

            }
            isBetween(currTime, snacksStart, snacksEnd) -> {
                Log.wtf("lkjk","snackstime")
                db.collection("Menu/Snacks/Rollu Dhaba")
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            Log.d(TAG, "${document.id} => ${document.data}")
                            document.getString("name")?.let { ItemName(it,
                                document.getString("description")!!, document.getString("price")!!, document.getBoolean("isVeg")!!, "Rollu Dhaba"
                            ) }?.let { arrayListBuffet.add(it)
                                Log.wtf("QQQQQ","Items are added")
                                if (arrayListBuffet.size == result.size()){
                                    adapter.add(StallNameItem("Rollu Dhaba"))
                                    Log.wtf("QQQQQ","Size of arrayList is ${arrayListBuffet.size}")
                                    for (i in 0 until arrayListBuffet.size){
                                        adapter.add(arrayListBuffet[i])
                                    }
                                }
                            }
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d(TAG, "Error getting documents: ", exception)
                    }
                db.collection("Menu/Snacks/Roll Maal")
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            Log.d(TAG, "${document.id} => ${document.data}")
                            document.getString("name")?.let { ItemName(it,
                                document.getString("description")!!, document.getString("price")!!, document.getBoolean("isVeg")!!,"Roll Maal"
                            ) }?.let { arrayListAla.add(it)
                                if (arrayListAla.size == result.size()){
                                    adapter.add(StallNameItem("Roll Maal"))
                                    if (arrayListAla.size != 0)
                                        for (i in 0 until arrayListAla.size){
                                            adapter.add(arrayListAla[i])
                                        }
                                }
                            }
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d(TAG, "Error getting documents: ", exception)
                    }


            }
            isBetween(currTime, dinnerStart, dinnerEnd) -> {
                Log.wtf("lkjk","dinnertime")

                db.collection("Menu/Dinner/Buffet")
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            Log.d(TAG, "${document.id} => ${document.data}")
                            document.getString("name")?.let { ItemName(it,
                                document.getString("description")!!, document.getString("price")!!, document.getBoolean("isVeg")!!,"Buffet"
                            ) }?.let { arrayListBuffet.add(it)
                                Log.wtf("QQQQQ","Items are added")
                                if (arrayListBuffet.size == result.size()){
                                    adapter.add(StallNameItem("Buffet"))
                                    Log.wtf("QQQQQ","Size of arrayList is ${arrayListBuffet.size}")
                                    for (i in 0 until arrayListBuffet.size){
                                        adapter.add(arrayListBuffet[i])
                                    }
                                }
                            }
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d(TAG, "Error getting documents: ", exception)
                    }
                db.collection("Menu/Dinner/Ala-Carte")
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            Log.d(TAG, "${document.id} => ${document.data}")
                            document.getString("name")?.let { ItemName(it,
                                document.getString("description")!!, document.getString("price")!!, document.getBoolean("isVeg")!!, "Ala-Carte"
                            ) }?.let { arrayListAla.add(it)
                                if (arrayListAla.size == result.size()){
                                    adapter.add(StallNameItem("Ala-Carte"))
                                    if (arrayListAla.size != 0)
                                        for (i in 0 until arrayListAla.size){
                                            adapter.add(arrayListAla[i])
                                        }
                                }
                            }
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d(TAG, "Error getting documents: ", exception)
                    }

                db.collection("Menu/Dinner/Roll Maal")
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            Log.d(TAG, "${document.id} => ${document.data}")

                            document.getString("name")?.let { ItemName(it,
                                document.getString("description")!!, document.getString("price")!!, document.getBoolean("isVeg")!!, "Roll Maal"
                            ) }?.let { arrayListRoll.add(it)
                                if (arrayListRoll.size == result.size()){
                                    adapter.add(StallNameItem("Roll Maal"))
                                    if (arrayListRoll.size != 0)
                                        for (i in 0 until arrayListRoll.size){
                                            adapter.add(arrayListRoll[i])
                                        }
                                }
                            }
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d(TAG, "Error getting documents: ", exception)
                    }




            }
            isBetween(currTime, midnightMessStart, midnightMessEnd) -> Log.wtf("lkjk","midnight time")
            else -> Log.wtf("lkjk","nothing availabnle")
        }

        db.collection("Menu/Bakery/Items")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")

                    document.getString("name")?.let { ItemName(it,
                        document.getString("description")!!, document.getString("price")!!, document.getBoolean("isVeg")!!, "Bakery"
                    ) }?.let { arrayListBakery.add(it)
                        if (arrayListBakery.size == result.size()){
                            adapter.add(StallNameItem("Bakery"))
                            if (arrayListBakery.size != 0)
                                for (i in 0 until arrayListBakery.size){
                                    adapter.add(arrayListBakery[i])
                                }
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }



        return view
    }


    fun isBetween(candidate: LocalTime, start: LocalTime, end: LocalTime): Boolean {
        return !candidate.isBefore(start) && !candidate.isAfter(end)
    }
}


class AddToCartDialog(val name : String, val description: String , val price: String, val itemName: ItemName) : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.add_to_cart_layout, container)

        dialog.window.setBackgroundDrawable( ColorDrawable(Color.TRANSPARENT))


        this.dialog.setTitle("Add to Cart")
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
            if (num == 1){
                return@setOnClickListener
            }
            num--
            qtyView.text = num.toString()
            var currPrice = price.toFloat()*num
            priceText.text = "Rs."+currPrice
        }

        var addButton = rootView.findViewById<Button>(R.id.add_to_cart_button)
        addButton.setOnClickListener {
            var qtt = qtyView.text.toString().toLong()
            for (i in 0 until cartitemsList.size){
                if (cartitemsList[i].itemName.name.equals(itemName.name)){
                    cartitemsList[i].qty += qtt
                    this.dismiss()
                    return@setOnClickListener
                }
            }
            cartitemsList.add(CartItems(qtt, itemName))
            this.dismiss()
        }

        return rootView
    }
}

class CartItems(var qty: Long, var itemName: ItemName)