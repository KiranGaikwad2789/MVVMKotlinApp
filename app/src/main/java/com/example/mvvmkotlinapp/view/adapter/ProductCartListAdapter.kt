package com.example.mvvmkotlinapp.view.adapter

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmkotlinapp.BR
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.databinding.RowProductCartListBinding
import com.example.mvvmkotlinapp.model.ProductOrderModel
import com.example.mvvmkotlinapp.viewmodel.ProductListViewModel

class ProductCartListAdapter (private val context: Context, private val arrayListProduct: List<ProductOrderModel>?) : RecyclerView.Adapter<ProductCartListAdapter.ViewHolder>(){

    //For update query imp
    // https://stackoverflow.com/questions/45789325/update-some-specific-field-of-an-entity-in-android-room
    lateinit var productListViewModel: ProductListViewModel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        productListViewModel = ViewModelProviders.of((context as FragmentActivity)).get(ProductListViewModel::class.java)
        val binding: RowProductCartListBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.row_product_cart_list, parent, false)
        binding.productList=productListViewModel
        return ViewHolder(binding)
    }


    override fun getItemCount(): Int {
        return arrayListProduct?.size!!
    }

    // Bind data
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(arrayListProduct!!.get(position))

        holder.binding.imgProductAdd.setOnClickListener { updateProduct(arrayListProduct!![position],"add") }
        holder.binding.imgProductMinus.setOnClickListener {
            if (arrayListProduct!![position].product_quantity!! >1)
            updateProduct(arrayListProduct!![position],"minus")
        }
        holder.binding.imgProductRemove.setOnClickListener { removeProduct(arrayListProduct!![position],"remove") }

    }

    // Creating ViewHolder
    class ViewHolder(val binding: RowProductCartListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: ProductOrderModel) {
            binding.setVariable(BR.product, data) //BR - generated class; BR.product - 'product' is variable name declared in layout
            binding.executePendingBindings()
        }
    }


    fun updateProduct(productPOJO: ProductOrderModel,flag:String) {
        var productQuantity=0

        if (flag=="add")
         productQuantity= productPOJO.product_quantity!!.toInt()+1
        else if (flag=="minus")
         productQuantity= productPOJO.product_quantity!!.toInt()-1

        var productSelected= ProductOrderModel(productPOJO.uid,productPOJO.master_product_orderid,productPOJO?.product_id,
            productPOJO?.product_name,productPOJO?.prod_cat_id,productPOJO?.route_id,productPOJO?.outlet_id,
            productPOJO?.dist_id, productPOJO?.product_price?.toDouble()!!,productQuantity* productPOJO?.product_price?.toDouble()!!,productQuantity.toInt(),productPOJO?.product_compony,productPOJO?.status)

        productListViewModel.updateProductCart(productSelected)
        productListViewModel.updateProductSelectedQuantity(productPOJO.product_id,true,productQuantity)
        notifyDataSetChanged()
    }

    fun removeProduct(productPOJO: ProductOrderModel,flag:String) {

        val builder = AlertDialog.Builder(context)
        //set message for alert dialog
        builder.setMessage(R.string.removeproduct_message)
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Yes"){dialogInterface, which ->
            productListViewModel.removeProductCart(productPOJO.uid)
            productListViewModel.updateProductSelectedQuantity(productPOJO.product_id,false,0)
            notifyDataSetChanged()
            dialogInterface.dismiss()
        }

        //performing negative action
        builder.setNegativeButton("No"){dialogInterface, which ->
            dialogInterface.dismiss()
        }

        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

}