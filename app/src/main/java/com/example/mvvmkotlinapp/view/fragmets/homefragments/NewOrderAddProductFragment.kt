package com.example.mvvmkotlinapp.view.fragmets.homefragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.common.RecyclerItemClickListenr
import com.example.mvvmkotlinapp.databinding.FragmentNewOrderAddProductBinding
import com.example.mvvmkotlinapp.model.ProductOrderModel
import com.example.mvvmkotlinapp.repository.room.tables.Product
import com.example.mvvmkotlinapp.view.adapter.NewOrderProductListAdapter
import com.example.mvvmkotlinapp.view.fragmets.homefragments.ProductCartFragment.Companion.arryListproductSelected
import com.example.mvvmkotlinapp.viewmodel.ProductListViewModel


class NewOrderAddProductFragment : Fragment() {


    lateinit var productListViewModel: ProductListViewModel
    lateinit var bindingAddProduct: FragmentNewOrderAddProductBinding
    private var adapter: NewOrderProductListAdapter? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        productListViewModel = ViewModelProviders.of(this).get(ProductListViewModel::class.java)
        bindingAddProduct = DataBindingUtil.inflate(inflater!!, R.layout.fragment_new_order_add_product, container, false)
        val view: View = bindingAddProduct.getRoot()
        bindingAddProduct.lifecycleOwner = this
        bindingAddProduct.productList=productListViewModel

        val viewProductNav = activity!!.findViewById<View>(R.id.navigationProduct)
        viewProductNav.visibility=View.GONE

        activity?.let {
            productListViewModel?.getProductList()?.observe(it, Observer<List<Product>> {
                this.setDataToAdapter(it)
            })
        }
        bindingAddProduct.btnAddProductToCart.setOnClickListener { addProductToCart() }

        return view
    }

    private fun addProductToCart() {
        arryListproductSelected?.let { productListViewModel?.insertSelectedProducts(it) }
        activity!!.onBackPressed()
    }

    private fun setDataToAdapter(arryListCity: List<Product>) {

        adapter = activity?.let { NewOrderProductListAdapter(it, arryListCity) }
        bindingAddProduct.recyclerViewProductCatList.addItemDecoration(
            DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        )
        bindingAddProduct.recyclerViewProductCatList!!.adapter = adapter
        bindingAddProduct.recyclerViewProductCatList!!.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        bindingAddProduct.recyclerViewProductCatList.setNestedScrollingEnabled(false);

    }
}
