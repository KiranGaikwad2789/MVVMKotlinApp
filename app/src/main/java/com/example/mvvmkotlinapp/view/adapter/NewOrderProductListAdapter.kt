package com.example.mvvmkotlinapp.view.adapter

import android.app.Dialog
import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.model.ProductOrderModel
import com.example.mvvmkotlinapp.repository.room.City
import com.example.mvvmkotlinapp.repository.room.tables.Product
import com.example.mvvmkotlinapp.view.fragmets.homefragments.ProductCartFragment.Companion.arryListproductSelected
import com.google.android.material.textfield.TextInputEditText
import java.util.*


class NewOrderProductListAdapter (ctx: Context, private var arrayListProductCat: List<Product>?) :
    RecyclerView.Adapter<NewOrderProductListAdapter.MyViewHolder>() {

    private val inflater: LayoutInflater
    private val arraylist: ArrayList<City>
    private var context: Context? =null

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
        holder.txtProductUnit.setText(this!!.arrayListProductCat!![position]?.product_quantity.toString())

        //in some cases, it will prevent unwanted situations
        holder.chkProductSelect!!.setOnCheckedChangeListener(null);

        if(arrayListProductCat!![position].product_isSelected!!){
            holder.chkProductSelect!!.setChecked(true)
        }

        holder.itemView.setOnClickListener {
            showProductQuantityDialog(arrayListProductCat!![position],holder,position)
        }
    }

    override fun getItemCount(): Int {
        return arrayListProductCat!!.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var txtProductName: TextView
        var txtProductPrice: TextView
        var txtProductUnit: TextView
        var chkProductSelect: CheckBox? = null

        init {
            txtProductName = itemView.findViewById(R.id.txtProductName) as TextView
            txtProductPrice = itemView.findViewById(R.id.txtProductPrice) as TextView
            txtProductUnit = itemView.findViewById(R.id.txtProductUnit) as TextView
            chkProductSelect = itemView.findViewById(R.id.chkProductSelect) as CheckBox
        }
    }

    private fun showProductQuantityDialog(
        productPOJO: Product?,
        holder: MyViewHolder,
        position: Int
    ) {
        val dialog = context?.let { Dialog(it, R.style.Theme_Dialog) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCancelable(false)
        dialog?.setContentView(R.layout.dialog_product_quantity)

        val edtProductQuantity = dialog?.findViewById(R.id.edtProductQuantity) as TextInputEditText
        val txtSaveProductQuantity = dialog?.findViewById(R.id.txtSaveProductQuantity) as TextView
        val txtCancelDialog = dialog?.findViewById(R.id.txtCancelDialog) as TextView

        edtProductQuantity.setText(productPOJO?.product_quantity.toString())

        txtSaveProductQuantity.setOnClickListener {
            //dialog?.dismiss()
            selectProduct(productPOJO,dialog,edtProductQuantity.text.toString().toInt(),holder,position)
        }
        txtCancelDialog.setOnClickListener { dialog .dismiss() }
        dialog?.show()
    }

    private fun selectProduct(productPOJO:Product?, dialog:Dialog, productQuantity:Int, holder: MyViewHolder, position: Int) {

        productPOJO?.product_isSelected =true
        productPOJO?.product_quantity =productQuantity
        holder.chkProductSelect!!.setChecked(true);
        notifyDataSetChanged()
        //Log.e("productPOJO: ",""+productPOJO)


        var productSelected= ProductOrderModel(0,0,productPOJO?.product_id,
            productPOJO?.product_name,productPOJO?.prod_cat_id,productPOJO?.route_id,productPOJO?.outlet_id,
            productPOJO?.dist_id, productPOJO?.product_price?.toDouble()!!,productQuantity* productPOJO?.product_price?.toDouble()!!,
            productQuantity.toInt(),productPOJO?.product_compony,productPOJO?.status,0)

        val event: ProductOrderModel? = arryListproductSelected!!.find { it.product_id == productPOJO?.product_id }
        Log.e("isContain: ",""+event)

        if(!TextUtils.isEmpty(event.toString()) || event!=null){
            arryListproductSelected!!.remove(event)
        }
        arryListproductSelected!!.add(productSelected!!)

        Log.e("Product selected size: ",""+ arryListproductSelected!!.size)
        Log.e("Product selected list: ",""+ arryListproductSelected!!.toString())


        dialog.dismiss()
    }
}