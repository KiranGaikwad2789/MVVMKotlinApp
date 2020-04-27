package com.example.mvvmkotlinapp.view.adapter

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.model.ProductOrderModel
import com.example.mvvmkotlinapp.repository.room.City
import com.example.mvvmkotlinapp.repository.room.tables.Product
import com.example.mvvmkotlinapp.view.fragmets.homefragments.NewOrderAddProductFragment
import com.google.android.material.textfield.TextInputEditText
import java.util.ArrayList

class NewOrderProductListAdapter (ctx: Context, private var arrayListProductCat: List<Product>?) :
    RecyclerView.Adapter<NewOrderProductListAdapter.MyViewHolder>() {

    private val inflater: LayoutInflater
    private val arraylist: ArrayList<City>
    private var context: Context? =null
    private var lastSelectedPosition = -1

    init {
        inflater = LayoutInflater.from(ctx)
        this.arraylist = ArrayList<City>()
        context=ctx
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = inflater.inflate(R.layout.row_neworder_product_list, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.txtProductName.setText(this!!.arrayListProductCat!![position]?.product_name)
        holder.txtProductPrice.setText(this!!.arrayListProductCat!![position]?.product_price.toString())
        //holder.chkProductSelect!!.setChecked(lastSelectedPosition == position);

        /*holder.itemView.setOnClickListener {
            Toast.makeText(context,""+ arrayListProductCat!![position]!!.product_name, Toast.LENGTH_SHORT).show()
            showProductQuantityDialog(arrayListProductCat!![position],holder,position)
        }*/
    }

    override fun getItemCount(): Int {
        return arrayListProductCat!!.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var txtProductName: TextView
        var txtProductPrice: TextView

        var chkProductSelect: CheckBox? = null

        init {
            txtProductName = itemView.findViewById(R.id.txtProductName) as TextView
            txtProductPrice = itemView.findViewById(R.id.txtProductPrice) as TextView
            chkProductSelect = itemView.findViewById(R.id.chkProductSelect) as CheckBox
        }
    }
}