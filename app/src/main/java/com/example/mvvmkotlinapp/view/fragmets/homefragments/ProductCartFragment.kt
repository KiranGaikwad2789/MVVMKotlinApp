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
import com.example.mvvmkotlinapp.databinding.FragmentProductCartBinding
import com.example.mvvmkotlinapp.model.ProductOrderModel
import com.example.mvvmkotlinapp.view.adapter.ProductCartListAdapter
import com.example.mvvmkotlinapp.viewmodel.ProductListViewModel
import kotlinx.android.synthetic.main.activity_product_select.view.*


class ProductCartFragment : Fragment() {

    lateinit var productListViewModel: ProductListViewModel
    lateinit var bindingProductCart: FragmentProductCartBinding
    private var adapter: ProductCartListAdapter? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        productListViewModel = ViewModelProviders.of(this).get(ProductListViewModel::class.java)
        bindingProductCart = DataBindingUtil.inflate(inflater!!, R.layout.fragment_product_cart, container, false)
        val view: View = bindingProductCart.getRoot()
        bindingProductCart.lifecycleOwner = this
        bindingProductCart.productList=productListViewModel
        // Inflate the layout for this fragment

        val viewProductNav = activity!!.findViewById<View>(R.id.navigationProduct)
        viewProductNav.visibility=View.VISIBLE

        activity?.let {
            productListViewModel?.getSelectedProductList()?.observe(it, Observer<List<ProductOrderModel>> {
                Log.e("Product cart list ",""+it.size)
                this.setDataToAdapter(it)
            })
        }


        viewProductNav.navigationProduct.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_apply_product_offer-> {
                  Toast.makeText(context,"No Offer available",Toast.LENGTH_SHORT).show()
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.nav_confirm_product_order-> {5

                    return@setOnNavigationItemSelectedListener true
                }

                R.id.nav_discard_product_offer-> {
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.nav_prod_offer_detail-> {
                    return@setOnNavigationItemSelectedListener true
                }

            }
            false

        }


        return view
    }

    private fun confirmOrderRemarkAddDialog() {


    }


    private fun setDataToAdapter(arryListCity: List<ProductOrderModel>) {

        var totalPrice=0.0
        var totalProductQuantity=0
        for(product in arryListCity){
            Log.e("Cart product details:",""+product)
            totalPrice+= product.product_total_price!!
            totalProductQuantity+= product.product_quantity!!
        }
        bindingProductCart.txtTotalPrice.text= totalPrice.toString()
        bindingProductCart.txtTotalProductCount.text= totalProductQuantity.toString()


        bindingProductCart.recyclerProductCartList.removeItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        adapter = activity?.let { ProductCartListAdapter(it, arryListCity) }
        bindingProductCart.recyclerProductCartList.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        bindingProductCart.recyclerProductCartList!!.adapter = adapter
        bindingProductCart.recyclerProductCartList.layoutManager = LinearLayoutManager(activity)
        bindingProductCart.recyclerProductCartList.setNestedScrollingEnabled(false);
    }

}
