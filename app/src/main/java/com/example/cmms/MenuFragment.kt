package com.example.cmms

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.menu_fragment.*


open class MenuFragment : Fragment(){
    val TAG = "Jab"
    val db = FirebaseFirestore.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val reference = FirebaseDatabase.getInstance().getReference("/Menu/Breakfast/Buffet")
        // Access a Cloud Firestore instance from your Activity
        val v = inflater.inflate(R.layout.menu_fragment, null)
        var breakfastItemTextView = v.findViewById<TextView>(R.id.breakfast_item_1)
        var vegLunch = v.findViewById<TextView>(R.id.veg_lunch_description)
        var nonveglunch = v.findViewById<TextView>(R.id.non_veg_lunch_description)
        var nonvegDinner = v.findViewById<TextView>(R.id.non_veg_dinner_description)
        var vegDinner = v.findViewById<TextView>(R.id.veg_dinner_description)
        var specialDinner = v.findViewById<TextView>(R.id.special_description)
        var menuLabel = v.findViewById<TextView>(R.id.menu_label)

        try {
            val activity = activity
            if (isAdded && activity != null) {
                var docRef = db.collection("Menu/Breakfast/Buffet").document("Item")

                docRef.get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            Log.wtf("asdfasdf", "DocumentSnapshot data: ${document.data}")
                            breakfastItemTextView.text = document.getString("description")
                        } else {
                            Log.wtf(TAG, "No such document")
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.wtf(TAG, "get failed with ", exception)
                    }

                docRef = db.collection("Menu/Lunch/Buffet").document("Veg")
                docRef.get().addOnSuccessListener { document ->
                    if (document != null) {
                        Log.wtf("asdfasdf", "DocumentSnapshot data: ${document.data}")
                        vegLunch.text = document.getString("description")
                    } else {
                        Log.wtf(TAG, "No such document")
                    }
                }
                    .addOnFailureListener { exception ->
                        Log.wtf(TAG, "get failed with ", exception)
                    }

                docRef = db.collection("Menu/Lunch/Buffet").document("Non Veg")
                docRef.get().addOnSuccessListener { document ->
                    if (document != null) {
                        Log.wtf("asdfasdf", "DocumentSnapshot data: ${document.data}")
                        nonveglunch.text = document.getString("description")
                    } else {
                        Log.wtf(TAG, "No such document")
                    }
                }
                    .addOnFailureListener { exception ->
                        Log.wtf(TAG, "get failed with ", exception)
                    }

                docRef = db.collection("Menu/Dinner/Buffet").document("Non Veg")
                docRef.get().addOnSuccessListener { document ->
                    if (document != null) {
                        Log.wtf("asdfasdf", "DocumentSnapshot data: ${document.data}")
                        nonvegDinner.text = document.getString("description")
                    } else {
                        Log.wtf(TAG, "No such document")
                    }
                }
                    .addOnFailureListener { exception ->
                        Log.wtf(TAG, "get failed with ", exception)
                    }

                docRef = db.collection("Menu/Dinner/Buffet").document("Veg")
                docRef.get().addOnSuccessListener { document ->
                    if (document != null) {
                        Log.wtf("asdfasdf", "DocumentSnapshot data: ${document.data}")
                        vegDinner.text = document.getString("description")
                    } else {
                        Log.wtf(TAG, "No such document")
                    }
                }
                    .addOnFailureListener { exception ->
                        Log.wtf(TAG, "get failed with ", exception)
                    }

                docRef = db.collection("Menu/Dinner/Buffet").document("Special")
                docRef.get().addOnSuccessListener { document ->
                    if (document != null) {
                        Log.wtf("asdfasdf", "DocumentSnapshot data: ${document.data}")
                        specialDinner.text = document.getString("description")
                    } else {
                        Log.wtf(TAG, "No such document")
                    }
                }
                    .addOnFailureListener { exception ->
                        Log.wtf(TAG, "get failed with ", exception)
                    }

            }


        }
        catch (e :Exception){

        }



        return v
    }

}

