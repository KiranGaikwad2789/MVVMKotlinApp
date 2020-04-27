package com.example.mvvmkotlinapp.view.fragmets.homefragments

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.common.DateTime
import com.example.mvvmkotlinapp.databinding.FragmentProductCartBinding
import com.example.mvvmkotlinapp.model.ProductOrderModel
import com.example.mvvmkotlinapp.repository.room.tables.MasterProductOrder
import com.example.mvvmkotlinapp.view.adapter.ProductCartListAdapter
import com.example.mvvmkotlinapp.viewmodel.ProductListViewModel
import kotlinx.android.synthetic.main.activity_product_select.view.*


class ProductCartFragment : Fragment() {


    lateinit var productListViewModel: ProductListViewModel
    lateinit var bindingProductCart: FragmentProductCartBinding
    private var adapter: ProductCartListAdapter? = null
    var totalPrice=0.0
    var totalProductQuantity=0
    var commaSeperatedString: String? =null
    private var currentDate: DateTime? =null
    var arryListProductCart: ArrayList<ProductOrderModel>? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        productListViewModel = ViewModelProviders.of(this).get(ProductListViewModel::class.java)
        bindingProductCart = DataBindingUtil.inflate(inflater, R.layout.fragment_product_cart, container, false)
        val view: View = bindingProductCart.getRoot()
        bindingProductCart.lifecycleOwner = this
        bindingProductCart.productList=productListViewModel

        currentDate= DateTime()
        val viewProductNav = activity!!.findViewById<View>(R.id.navigationProduct)
        viewProductNav.visibility=View.VISIBLE

        activity?.let {
            productListViewModel.getSelectedProductList()?.observe(it, Observer<List<ProductOrderModel>> {
                Log.e("Product cart list ",""+it.size)
                arryListProductCart?.addAll(it)
                this.setDataToAdapter(it)
            })
        }


        viewProductNav.navigationProduct.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_apply_product_offer-> {
                  Toast.makeText(context,"No Offer available",Toast.LENGTH_SHORT).show()
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.nav_confirm_product_order-> {
                    confirmOrderRemarkAddDialog()
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun confirmOrderRemarkAddDialog() {
        val builder = AlertDialog.Builder(context)
        //set message for alert dialog
        builder.setTitle(R.string.confirm_order_title)
        builder.setMessage(R.string.confirm_order_message)

        //performing positive action
        builder.setPositiveButton("Yes"){dialogInterface, which ->

            var masterProductOrder= MasterProductOrder(0,
                commaSeperatedString,1,1,1,totalPrice,totalProductQuantity,
                currentDate?.getDateTime(),"Pending")
            Log.e("masterProductOrder: ",""+ masterProductOrder)

            var result = productListViewModel.addNewMasterProductOrder(masterProductOrder,arryListProductCart)
            Log.e("masterProductOrder result: ",""+ result)
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


    private fun setDataToAdapter(arryListCity: List<ProductOrderModel>) {

        for(product in arryListCity){
            Log.e("Cart product details:",""+product)
            totalPrice+= product.product_total_price!!
            totalProductQuantity+= product.product_quantity!!
        }

        commaSeperatedString = arryListCity.joinToString { it -> "${it.uid}" }
        Log.e("cartIds:",""+commaSeperatedString)

        bindingProductCart.txtTotalPrice.text= totalPrice.toString()
        bindingProductCart.txtTotalProductCount.text= totalProductQuantity.toString()


        bindingProductCart.recyclerProductCartList.removeItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        adapter = activity?.let { ProductCartListAdapter(it, arryListCity) }
        bindingProductCart.recyclerProductCartList.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        bindingProductCart.recyclerProductCartList.adapter = adapter
        bindingProductCart.recyclerProductCartList.layoutManager = LinearLayoutManager(activity)
        bindingProductCart.recyclerProductCartList.setNestedScrollingEnabled(false);
    }

}
