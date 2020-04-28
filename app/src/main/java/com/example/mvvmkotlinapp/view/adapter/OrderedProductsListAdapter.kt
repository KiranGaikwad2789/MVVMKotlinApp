package com.example.mvvmkotlinapp.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmkotlinapp.BR
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.databinding.RowOrderedProductsListBinding
import com.example.mvvmkotlinapp.model.ProductOrderModel
import com.example.mvvmkotlinapp.viewmodel.ProductListViewModel

class OrderedProductsListAdapter (private val context: Context, private val arrayListProduct: List<ProductOrderModel>?) : RecyclerView.Adapter<OrderedProductsListAdapter.ViewHolder>(){

    lateinit var productListViewModel: ProductListViewModel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        productListViewModel = ViewModelProviders.of((context as FragmentActivity)).get(ProductListViewModel::class.java)
        val binding: RowOrderedProductsListBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.row_ordered_products_list, parent, false)
        binding.productList=productListViewModel
        return ViewHolder(binding)
    }


    override fun getItemCount(): Int {
        return arrayListProduct?.size!!
    }

    // Bind data
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(arrayListProduct!!.get(position))
    }

    // Creating ViewHolder
    class ViewHolder(val binding: RowOrderedProductsListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ProductOrderModel) {
            binding.setVariable(BR.orderedProducts, data) //BR - generated class; BR.product - 'product' is variable name declared in layout
            binding.executePendingBindings()
        }
    }
}