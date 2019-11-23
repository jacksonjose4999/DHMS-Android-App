package com.example.cmms

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toolbar

import androidx.appcompat.app.ActionBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.order_fragment.*

class MainActivity : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener{

    companion object{
        var rollNoUser : String = ""
        var availableBalance : Double = 0.0
        var daysRemaining : Int = 30
        var nameUser: String = ""
        var netID: String = ""
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        val fragment : Fragment? = null
        supportFragmentManager.beginTransaction().replace(R.id.frame_container,OrderFragment()).commit()
        return false
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        var f : Fragment? = null
        when (item.itemId) {
            R.id.order_now -> {
                f = OrderFragment()
                Log.wtf("fjfjfj","order")
                if(actionBar != null){
                    actionBar.title = "Order"
                }
                supportFragmentManager.beginTransaction().replace(R.id.frame_container,f!!).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.receipt_bottom -> {
                f = ReceiptFragment()
                supportFragmentManager.beginTransaction().replace(R.id.frame_container,f!!).commit()

                return@OnNavigationItemSelectedListener true
            }
            R.id.cart_bottom -> {
                f = CartFragment()
                supportFragmentManager.beginTransaction().replace(R.id.frame_container,f!!).commit()

                return@OnNavigationItemSelectedListener true
            }
            R.id.menu_bottom -> {
                f = MenuFragment()
                if(actionBar != null){
                    actionBar.title = "Menu"
                }
                supportFragmentManager.beginTransaction().replace(R.id.frame_container,f!!).commit()

                return@OnNavigationItemSelectedListener true
            }
            R.id.passbook_bottom -> {
                f = PassbookFragment()
                supportFragmentManager.beginTransaction().replace(R.id.frame_container,f!!).commit()

                return@OnNavigationItemSelectedListener true
            }
        }


        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)


        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val name = user.displayName
            val email = user.email
            val uid = user.uid // The user's ID, unique to the Firebase project.
            var netId = email
            var db = FirebaseFirestore.getInstance()
            Log.wtf("sksk","$netId")

            var f = PassbookFragment()
            supportFragmentManager.beginTransaction().replace(R.id.frame_container,f!!).commit()

            if (email!=null) {
                for (i in email.indices) {
                    if (email[i] == '@'){
                        netId = email.substring(0,i)
                        netID = netId
                        Log.wtf("sksk","$netId")

                        db.collection("Users")
                            .get()
                            .addOnSuccessListener { result ->
                                for (document in result) {
                                    if(document.id.toString().equals(netId)){
                                        nameUser = document.getString("name").toString()
                                        rollNoUser = document.getString("roll number").toString()
                                        availableBalance = document.getDouble("balance") as Double
                                    }
                                }
                            }
                            .addOnFailureListener { exception ->
                                Log.d(ContentValues.TAG, "Error getting documents: ", exception)
                            }
                    }
                }
            }
        }



        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        if (FirebaseAuth.getInstance().currentUser == null){
            startActivity(Intent(this, GoogleLoginActivity::class.java))
        }
    }

}


/*private fun signOut() {
      var auth = FirebaseAuth.getInstance()
      // Firebase sign out
      auth.signOut()
      val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
          .requestIdToken(getString(R.string.default_web_client_id))
          .requestEmail()
          .build()

      var googleSignInClient = GoogleSignIn.getClient(this, gso)
      googleSignInClient.signOut()
  }*/