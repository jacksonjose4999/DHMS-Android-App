package com.example.cmms

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseUser
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.passbook_fragment.*


open class PassbookFragment : Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var v = inflater.inflate(R.layout.passbook_fragment, null)
        var passbookNameView = v.findViewById<TextView>(R.id.passbook_name_display)
        var passbookRollView = v.findViewById<TextView>(R.id.roll_number_passbook)
        var passbookAvailableBalance = v.findViewById<TextView>(R.id.passbook_available_balance)

        Log.e("sksk","netId")

        var signOut = v.findViewById<Button>(R.id.sign_out)
        signOut.setOnClickListener {
            signOut()
            Log.wtf("sksk","netId")

        }
        Log.wtf("sksk","netId")

        var db = FirebaseFirestore.getInstance()

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val name = user.displayName
            val email = user.email
            val uid = user.uid // The user's ID, unique to the Firebase project.
            var netId = email
            Log.wtf("sksk","$netId")

            if (email!=null) {
                for (i in email.indices) {
                    if (email[i] == '@'){
                        netId = email.substring(0,i)
                        Log.wtf("sksk","$netId")

                        db.collection("Users")
                            .get()
                            .addOnSuccessListener { result ->
                                for (document in result) {
                                        if(document.id.toString().equals(netId)){
                                            passbookNameView.text = "Good Afternoon\n"+document.getString("name")+"!"
                                            passbookRollView.text = document.getString("roll number")
                                            passbookAvailableBalance.text =  "%.2f".format((document.getDouble("balance") as Double))
                                        }
                                    }
                                }
                            .addOnFailureListener { exception ->
                                Log.d(ContentValues.TAG, "Error getting documents: ", exception)
                            }
                    }
                }
            }
        } else {
            startActivity(Intent(this.context, GoogleLoginActivity::class.java))
        }
        var adapter = GroupAdapter<ViewHolder>()

        var recyclerView = v.findViewById<RecyclerView>(R.id.recycler_view_passbook_history)
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView!!.adapter = adapter

        db.collection("Users/${MainActivity.netID}/Orders")
            .get()
            .addOnSuccessListener { result->
                for (document in result){
                    Log.wtf("hjkhjkhjkl",document.getString("name")+" "+document.getString("price"))

                    var passbookHistory = document.getString("name")?.let {
                        document.getLong("quantity")?.let { it1 ->
                            PassbookHistory(it,
                                document.getString("price")!!, it1
                            )
                        }
                    }

                    if (passbookHistory != null) {
                        adapter.add(passbookHistory)
                    }
                }
            }


        return v
    }



    private fun signOut() {
      var auth = FirebaseAuth.getInstance()
      // Firebase sign out
      auth.signOut()
      val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
          .requestIdToken(getString(R.string.default_web_client_id))
          .requestEmail()
          .build()

      var googleSignInClient = this.context?.let { GoogleSignIn.getClient(it, gso) }
        googleSignInClient?.signOut()
        startActivity(Intent(this.context, GoogleLoginActivity::class.java))
  }
}