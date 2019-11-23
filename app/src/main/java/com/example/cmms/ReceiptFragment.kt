package com.example.cmms

import android.graphics.Bitmap
import android.os.Bundle
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.qr_code_receipt_fragment.*
import java.lang.Exception
import java.lang.StringBuilder

open class ReceiptFragment : Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var v = inflater.inflate(R.layout.receipt_fragment, null)
        var adapterAla = GroupAdapter<ViewHolder>()
        var db = FirebaseFirestore.getInstance()

        var recyclerViewAla = v.findViewById<RecyclerView>(R.id.recycler_view_alacarte_receipt)
        recyclerViewAla.layoutManager = LinearLayoutManager(this.context)
        recyclerViewAla!!.adapter = adapterAla

        adapterAla.setOnItemClickListener { item, view ->
            try {
                var array = ArrayList<String>()
                db.collection("Users/${MainActivity.netID}/Orders")
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            array.add(document.id)
                        }
                        val qrCodeReceipt = QRCodeReceipt(array)
                        qrCodeReceipt.show(fragmentManager, "Receipt")
                    }
            }
            catch ( e : Exception){

            }
        }



        var adapterBuff = GroupAdapter<ViewHolder>()
        var recyclerViewBuff = v.findViewById<RecyclerView>(R.id.recycler_view_buffet_receipt)
        recyclerViewBuff.layoutManager = LinearLayoutManager(this.context)
        recyclerViewBuff!!.adapter = adapterBuff

        var adapterRoll = GroupAdapter<ViewHolder>()
        var recyclerRoll = v.findViewById<RecyclerView>(R.id.recycler_view_roll_maal_receipt)
        recyclerRoll.layoutManager = LinearLayoutManager(this.context)
        recyclerRoll!!.adapter = adapterRoll

        var adapterBakery = GroupAdapter<ViewHolder>()
        var recyclerViewBakery = v.findViewById<RecyclerView>(R.id.recycler_view_bakery_receipt)
        recyclerViewBakery.layoutManager = LinearLayoutManager(this.context)
        recyclerViewBakery!!.adapter = adapterBakery

        adapterBuff.setOnItemClickListener { item, view ->
            try {
                var array = ArrayList<String>()

                db.collection("Users/${MainActivity.netID}/Orders")
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            array.add(document.id)
                        }
                        val qrCodeReceipt = QRCodeReceipt(array)
                        qrCodeReceipt.show(fragmentManager, "Receipt")
                    }
            }
            catch ( e : Exception){

            }
        }
        adapterRoll.setOnItemClickListener { item, view ->
            try {
                var array = ArrayList<String>()
                db.collection("Users/${MainActivity.netID}/Orders")
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            array.add(document.id)
                        }
                        val qrCodeReceipt = QRCodeReceipt(array)
                        qrCodeReceipt.show(fragmentManager, "Receipt")
                    }
            }
            catch ( e : Exception){

            }
        }

            adapterBakery.setOnItemClickListener { item, view ->
                try {
                    var array = ArrayList<String>()
                    db.collection("Users/${MainActivity.netID}/Orders")
                        .get()
                        .addOnSuccessListener { result ->
                            for (document in result) {
                                array.add(document.id)
                            }
                            val qrCodeReceipt = QRCodeReceipt(array)
                            qrCodeReceipt.show(fragmentManager, "Receipt")
                        }
                }
                catch ( e : Exception){

                }
            }


        db.collection("Users/${MainActivity.netID}/Orders")
            .get()
            .addOnSuccessListener { result->
                for (document in result){
                    if (document.getString("stall").equals("Buffet")){

                        document.getLong("quantity")?.let { document.getString("name")?.let { it1 ->
                            ReceiptItems(it,
                                it1
                            )
                        } }?.let {
                            adapterBuff.add(it)
                        }
                    }
                    if (document.getString("stall").equals("Roll Maal")){
                        document.getLong("quantity")?.let { document.getString("name")?.let { it1 ->
                            ReceiptItems(it,
                                it1
                            )
                        } }?.let {
                            adapterRoll.add(it)
                        }

                    }
                    if (document.getString("stall").equals("Ala-Carte")){
                        document.getLong("quantity")?.let { document.getString("name")?.let { it1 ->
                            ReceiptItems(it,
                                it1
                            )
                        } }?.let {
                            adapterAla.add(it)
                        }

                    }
                    if (document.getString("stall").equals("Bakery")){
                        document.getLong("quantity")?.let { document.getString("name")?.let { it1 ->
                            ReceiptItems(it,
                                it1
                            )
                        } }?.let {
                            adapterBakery.add(it)
                        }

                    }
                }
            }

        return v
    }
}


class QRCodeReceipt(val itemList: ArrayList<String>) : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.qr_code_receipt_fragment, container)
        var imageView = rootView.findViewById<ImageView>(R.id.qr_codeimage)

        var endcoding = StringBuilder()
        for (i in itemList){
            endcoding.append("$i ")
        }

        var multiFormatWriter = MultiFormatWriter()

        try{
            var bitMatrix = multiFormatWriter.encode(endcoding.toString(), BarcodeFormat.QR_CODE,200,200)
            var barcodeEndoder = BarcodeEncoder()
            var bitmap = barcodeEndoder.createBitmap(bitMatrix)
            imageView.setImageBitmap(bitmap)
        }
        catch (e: WriterException){

        }

        return rootView
    }
}
